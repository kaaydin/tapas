package ch.unisg.tapascalculator.services;

import ch.unisg.tapascalculator.domain.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class StatusService {
    private static final Logger LOGGER = LogManager.getLogger(StatusService.class);

    public void publishTaskStartedEvent(String taskLocation) {
        publishTaskStatusEvent(taskLocation, Task.Status.RUNNING, null);
    }
    public void publishTaskStatusEvent(String taskLocation, String taskStatus, @Nullable String output) {
        LOGGER.info("Status " + output);

        String patch = "[\n { \"op\": \"replace\", \"path\": \"/taskStatus\", \"value\": \"" + taskStatus + "\" }";

        if(taskStatus.equals(Task.Status.EXECUTED))
            patch += ", { \"op\": \"add\", \"path\": \"/outputData\", \"value\": \"" + output + "\" }\n";

        patch += "]";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json-patch+json")
                .uri(URI.create(taskLocation))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(patch))
                .build();
        LOGGER.info("Payload Out: " + patch);

        // Needs the other service running
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
