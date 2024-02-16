package ch.unisg.tapasmirocard;


import ch.unisg.ics.interactions.wot.td.ThingDescription;
import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.affordances.PropertyAffordance;
import ch.unisg.ics.interactions.wot.td.clients.TDCoapRequest;
import ch.unisg.ics.interactions.wot.td.clients.TDCoapResponse;
import ch.unisg.ics.interactions.wot.td.io.TDGraphReader;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import ch.unisg.tapasmirocard.domain.Task;
import ch.unisg.tapasmirocard.services.ExecutionUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This executor can retrieve different sensor data from the Mirogate sensor.
 * It takes as an input the type of data which should be queried (e.g. humidity or temperature),
 * finds the Mirogate URI by querying a SPARQL search engine and then creates a CoAP request
 * based on the Thing Description of Mirogate.
 */
public class MirocardExecutor implements ExecutionUseCase {
    private static final Logger LOGGER = LogManager.getLogger(MirocardExecutor.class);

    @Override
    public CompletableFuture<Task> executeTask(final Task task) {
        return CompletableFuture.supplyAsync(() -> {
            // Query SPARQL search engine
            String sparqlQuery = buildSparqlQuery();
            String sparqlResponse = fetchSparqlResponse(sparqlQuery);
            try {
                // Parse tdCoapResponse
                ThingDescription td = TDGraphReader.readFromURL(ThingDescription.TDFormat.RDF_TURTLE, parseSparqlResponse(sparqlResponse));
                // Execute CoAp Request
                Optional<String> potentialPayload = retrievePropertyWithCoapRequest(td, task);
                String payload = potentialPayload.get();
                payload = payload.replaceAll("[^\\d.]", "");
                LOGGER.info(String.format("Received payload: " + payload));
                task.setOutput(payload);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                LOGGER.error(String.format("Error when parsing: " + e.getMessage()));
            }
            return task;
        });
    }
    private String buildSparqlQuery() {
        return """
            @prefix td: <https://www.w3.org/2019/wot/td#>.
            select ?x
            where { ?x a <https://interactions.ics.unisg.ch/mirogate#Mirogate> }
            """;
    }
    private Optional<String> retrievePropertyWithCoapRequest(ThingDescription td, Task task) throws IOException {
        try{
            Optional<PropertyAffordance> property = td.getPropertyByName(task.getInput());
            if (property.isPresent()) {
                Optional<Form> form = property.get().getFirstFormForOperationType(TD.observeProperty);
                if (form.isPresent()) {
                    TDCoapRequest request = new TDCoapRequest(form.get(), TD.observeProperty);
                    TDCoapResponse tdCoapResponse = request.execute();
                    Optional<String> potentialPayload = tdCoapResponse.getPayload();
                    LOGGER.info(String.format("Received status and response: " + tdCoapResponse.getResponseCode() + " " + tdCoapResponse.getPayload()));
                    return potentialPayload;
                } else {
                    LOGGER.info(String.format("Could not find property %s in Thing Description!", property));
                }
            }
        } catch (IOException e) {
            LOGGER.info(String.format("Error during CoAP request: " + e.getMessage()));
        }
        return Optional.empty();
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
}
