package ch.unisg.tapasroster.roster.adapter.out.web;

import ch.unisg.tapasroster.roster.application.port.out.FindExecutorPort;
import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
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
    private static final Logger LOGGER = LogManager.getLogger(FindExecutorWebAdapter.class);

    @Override
    public ExecutorBaseUri findExecutor(String taskType) {
        ExecutorBaseUri selectedExecutorBaseUri = null;
        try {
            String executors = makeRequestToFindExecutors(taskType);

            JSONArray executorsArray = new JSONArray(executors);

            if (!executorsArray.isEmpty()) {
                JSONObject jsonObject = executorsArray.getJSONObject(0);
                selectedExecutorBaseUri = new ExecutorBaseUri(jsonObject.getString("executorBaseUri"));
                LOGGER.info("Found executor: " + selectedExecutorBaseUri.getBaseUri());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (WebClientRequestException e) {
            LOGGER.info("Could not reach executor pool: " + environment.getProperty("executorpool.uri") + "executors/type/" + taskType);
        }
        return selectedExecutorBaseUri;
    }


    private String makeRequestToFindExecutors(String taskType) throws IOException, InterruptedException {
        try {
            return WebClient.create()
                .get()
                .uri(environment.getProperty("executorpool.uri") + "executors/type/" + taskType)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    LOGGER.info("Successfully retrieved executors for task type: " + taskType);
                })
                .doOnError(error -> {
                    LOGGER.error("Error occurred while retrieving executors for task type: " + taskType, error);
                })
                .block();
        } catch (WebClientResponseException e) {
            LOGGER.error("WebClientResponseException: " + e.getMessage(), e);
            throw e;
        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: " + e.getMessage(), e);
            throw e;
        }
    }
}
