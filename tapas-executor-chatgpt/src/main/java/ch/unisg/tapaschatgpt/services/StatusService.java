package ch.unisg.tapaschatgpt.services;

import ch.unisg.tapaschatgpt.domain.Task;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class StatusService {

    public void publishTaskStartedEvent(String taskLocation) {
        publishTaskStatusEvent(taskLocation, Task.Status.RUNNING, null);
    }
    public void publishTaskStatusEvent(String taskLocation, String taskStatus, @Nullable String output) {
        System.out.println("Status " + output);

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
        System.out.println("Payload Out: " + patch);

        // Needs the other service running
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
