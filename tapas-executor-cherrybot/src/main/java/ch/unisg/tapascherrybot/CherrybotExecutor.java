package ch.unisg.tapascherrybot;

import ch.unisg.ics.interactions.wot.td.ThingDescription;
import ch.unisg.ics.interactions.wot.td.affordances.ActionAffordance;
import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.clients.TDHttpRequest;
import ch.unisg.ics.interactions.wot.td.clients.TDHttpResponse;
import ch.unisg.ics.interactions.wot.td.io.TDGraphReader;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import ch.unisg.ics.interactions.wot.td.schemas.ObjectSchema;
import ch.unisg.ics.interactions.wot.td.security.APIKeySecurityScheme;
import ch.unisg.ics.interactions.wot.td.security.SecurityScheme;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import ch.unisg.ics.interactions.wot.td.vocabularies.WoTSec;

import ch.unisg.tapascherrybot.domain.Task;
import ch.unisg.tapascherrybot.services.ExecutionUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CherrybotExecutor implements ExecutionUseCase {

    private static final Logger LOGGER = LogManager.getLogger(CherrybotExecutor.class);

    @Override
    public CompletableFuture<Task> executeTask(final Task task) {
        return CompletableFuture.supplyAsync(() -> {
            String inputPrompt = task.getInput();
            if (inputPrompt == null || inputPrompt.isEmpty()) {
                LOGGER.error("Input prompt is null or empty");
                return task;
            }
            try {
                LOGGER.info("New task: " + task.getTaskLocation());

                String sparqlQuery = buildSparqlQuery();
                String sparqlResponse = fetchSparqlResponse(sparqlQuery);

                var inputMap = parseTaskInput(task);
                String action = (String) inputMap.get("action");
                Object payload = inputMap.get("payload");

                ThingDescription td = TDGraphReader.readFromURL(ThingDescription.TDFormat.RDF_TURTLE, parseSparqlResponse(sparqlResponse));
                String token = registerOperator(td);
                processAction(td, action, payload, token, task);
                Thread.sleep(3000); //Got a 429 error sometimes, so we artifically wait a little
                deleteOperator(td, token);
            } catch (IOException e) {
                LOGGER.error("IO Exception in executeTask: " + e.getMessage(), e);
                throw new RuntimeException("IO error executing task", e);
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted Exception in executeTask: " + e.getMessage(), e);
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task execution interrupted", e);
            } catch (Exception e) {
                LOGGER.error("Unexpected error in executeTask", e);
                throw new RuntimeException("Error executing task", e);
            }
            return task;
        });
    }

    private String buildSparqlQuery() {
        return """
            @prefix td: <https://www.w3.org/2019/wot/td#>.
            select ?x
            where { ?x a <https://interactions.ics.unisg.ch/ufactory#PhysicalxArm7Robot> }
            """;
    }

    private String fetchSparqlResponse(String query) {
        try {
            String response = WebClient.create()
                    .post()
                    .uri("https://api.interactions.ics.unisg.ch/search/searchEngine")
                    .body(BodyInserters.fromValue(query))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Consider setting a timeout
            LOGGER.info("Response: " + response);
            return response;
        } catch (WebClientResponseException e) {
            LOGGER.error("Error in fetchSparqlResponse: " + e.getMessage());
            throw new RuntimeException("Error fetching SPARQL response", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in fetchSparqlResponse", e);
            throw new RuntimeException("Unexpected error fetching SPARQL response", e);
        }
    }

    private HashMap<String, Object> parseTaskInput(Task task) throws IOException {
        return new ObjectMapper().readValue(task.getInput(), HashMap.class);
    }
    private String parseSparqlResponse(String response) throws ParserConfigurationException, IOException, SAXException, RuntimeException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(response));
            Document doc = builder.parse(is);

            Node node = doc.getElementsByTagName("uri").item(0);
            if (node == null) {
                throw new RuntimeException("URI element not found in SPARQL response");
            }
            return node.getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error("Error parsing SPARQL response", e);
            throw new RuntimeException("Error parsing SPARQL response", e);
        }
    }

    private void processAction(ThingDescription td, String action, Object payload, String token, Task task) throws RuntimeException {
        SecurityScheme securitySchemeByType = td.getFirstSecuritySchemeByType(WoTSec.APIKeySecurityScheme)
                .orElseThrow(() -> new RuntimeException("No security Scheme"));
        Optional<ActionAffordance> actionByName = td.getActionByName(action);

        if (actionByName.isPresent()) {
            ActionAffordance actionAffordance = actionByName.get();
            Optional<Form> form = actionAffordance.getFirstForm();
            if (form.isPresent()) {
                TDHttpRequest tdHttpRequest = new TDHttpRequest(form.get(), TD.invokeAction);
                if (actionAffordance.getInputSchema().isPresent()) {
                    DataSchema dataSchema = actionAffordance.getInputSchema().get();
                    if (dataSchema.toString().contains("IntegerSchema")) {
                        Integer gripperInput = (Integer) payload;
                        tdHttpRequest.setPrimitivePayload(dataSchema, gripperInput);
                    }else {
                        HashMap tcpInput = (HashMap) payload;
                        tdHttpRequest.setObjectPayload((ObjectSchema) dataSchema, tcpInput);
                    }
                }
                tdHttpRequest.setAPIKey((APIKeySecurityScheme) securitySchemeByType, token);
                TDHttpResponse response = null;
                try {
                    response = tdHttpRequest.execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                task.setOutput("Response: "+ response.getStatusCode());
            }
        }
    }

    private String registerOperator(ThingDescription td) throws IOException {
        Optional<ActionAffordance> potentialRegisterAction = td.getActionByName("registerOperator");
        if (potentialRegisterAction.isPresent()) {
            ActionAffordance registerAction = potentialRegisterAction.get();
            Optional<Form> form = registerAction.getFirstForm();
            if (form.isPresent()) {
                TDHttpRequest tdHttpRequest = new TDHttpRequest(form.get(), TD.invokeAction);
                DataSchema dataSchema = registerAction.getInputSchema().get();
                LOGGER.info(tdHttpRequest);
                Map<String, Object> payload = new HashMap<>();
                payload.put("http://xmlns.com/foaf/0.1/Name", "Michael Bruelisauer");
                payload.put("http://xmlns.com/foaf/0.1/Mbox", "michael.bruelisauer@student.unisg.ch");
                tdHttpRequest.setObjectPayload((ObjectSchema) dataSchema, payload);
                LOGGER.info(tdHttpRequest);
                TDHttpResponse response = tdHttpRequest.execute();
                String location = response.getHeaders().get("location");
                int tokenIndex = location.lastIndexOf('/');
                String token = location.substring(tokenIndex + 1);
                LOGGER.info("Token: " + token);
                return token;
            }
        }
        return null;
    }
    private String deleteOperator(ThingDescription td, String token){
        Optional<ActionAffordance> potentialRegisterAction = td.getActionByName("removeOperator");
        if (potentialRegisterAction.isPresent()) {
            ActionAffordance deleteOepratorAction = potentialRegisterAction.get();
            Optional<Form> form = deleteOepratorAction.getFirstForm();
            if (form.isPresent()) {
                Form deleteForm = form.get();
                String baseUrl = deleteForm.getTarget();
                String finalUrl = baseUrl.replace("%7Btoken%7D", token);
                try {
                    URL url = new URL(finalUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE"); // Set method if it's not DELETE
                    int responseCode = conn.getResponseCode();
                    LOGGER.info("Delete Operator Response Code: " + responseCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}