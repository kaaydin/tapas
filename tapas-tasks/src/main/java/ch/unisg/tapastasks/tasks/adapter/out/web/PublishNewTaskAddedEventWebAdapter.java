package ch.unisg.tapastasks.tasks.adapter.out.web;

import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEvent;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEventPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.io.IOException;
import java.net.http.HttpResponse;


@Component
@Primary
public class PublishNewTaskAddedEventWebAdapter implements NewTaskAddedEventPort {
    private static final Logger LOGGER = LogManager.getLogger(PublishNewTaskAddedEventWebAdapter.class);

    @Autowired
    private Environment environment;

    String server = "http://tapas-roster:8083";

    @Override
    public void publishNewTaskAddedEvent(NewTaskAddedEvent event) {

        //Here we would need to work with DTOs in case the payload of calls becomes more complex
        LOGGER.info("Publishing new Task Added Event with id + " + event.taskId.getValue());
        var values = new HashMap<String, String>() {{
            put("taskLocation",environment.getProperty("baseuri")+"tasks/"+event.taskId.getValue());
            put("taskListName",event.taskListName.getValue());
            put("taskType", event.taskType.getValue());
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        LOGGER.info(requestBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/roster+task+json")
                .uri(URI.create(server+"/roster/newtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
