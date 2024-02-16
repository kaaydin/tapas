package ch.unisg.tapasmasonsmanifesto;

import ch.unisg.tapasmasonsmanifesto.domain.Task;
import ch.unisg.tapasmasonsmanifesto.services.ExecutionUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

@Service
public class MasonExecutor implements ExecutionUseCase {
    private static final Logger LOGGER = LogManager.getLogger(MasonExecutor.class);

    @Override
    public CompletableFuture<Task> executeTask(final Task task) {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Executing task " + task.getTaskLocation());

            // call tldr with input data
            String output = callMason();

            LOGGER.info(output);

            task.setOutput(output);
            return task;
        });
    }

    private String callMason() {
        String bearerToken = "79497d9a4822933def5fd0f20c90dd63d88cc447997217072e3a896235ef617fa4e530cc88fc573520bb24eebc4e4c6b";
        String apiEndpoint = "https://manifest-of-mason.vercel.app/api/v1/quote";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);

        // http entity with headers and payload
        HttpEntity<String> request = new HttpEntity<>(headers);

        // execute get request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiEndpoint, HttpMethod.GET, request, String.class);

        String output = response.getBody();
        LOGGER.info(output);

        return output;
    }
}
