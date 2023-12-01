package ch.unisg.tapasroster.roster.adapter.out.web;

import ch.unisg.tapasroster.roster.application.port.out.FindExecutorPort;
import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;


@Component
public class FindExecutorWebAdapter implements FindExecutorPort {
    private final Environment environment;

    public FindExecutorWebAdapter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public ExecutorBaseUri findExecutor(String taskType) {

        ExecutorBaseUri selectedExecutorBaseUri = null;

        try {
            String executors = makeRequestToFindExecutors(taskType);

            JSONArray executorsArray = new JSONArray(executors);

            if (!executorsArray.isEmpty()) {
                JSONObject jsonObject = executorsArray.getJSONObject(0);
                selectedExecutorBaseUri = new ExecutorBaseUri(jsonObject.getString("executorBaseUri"));
                System.out.println("Found executor: " + selectedExecutorBaseUri.getBaseUri());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (WebClientResponseException e) {
            if(e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                System.out.println("No available executor in executor pool for task type " + taskType);
            } else {
                System.out.println(e.getMessage());
            }
        } catch (WebClientRequestException e) {
            System.out.println("Could not reach executor pool: " + environment.getProperty("executorpool.uri") + "executors/type/" + taskType);
        }
        return selectedExecutorBaseUri;
    }


    private String makeRequestToFindExecutors(String taskType) throws IOException, InterruptedException {
        return WebClient.create()
            .get()
            .uri(environment.getProperty("executorpool.uri") + "executors/type/" + taskType)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
