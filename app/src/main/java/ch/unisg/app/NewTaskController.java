package ch.unisg.app;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

//Used to interact as a client with the TapasTasks Service (running locally) to add a new service.

@RestController
public class NewTaskController {

    //Used to receive a task as JSON via HTTP Post and call the TapasTasks service to add this task via HTTP Post in the standard way.
    @PostMapping(path = "/apptasks/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewTask(@RequestBody String payload) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Content-Type", "application/task+json")
                    .uri(URI.create("http://localhost:8081/tasks/"))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
                HttpHeaders headers = response.headers();
                List<String> locations = headers.allValues("Location");
                responseHeaders.add(org.springframework.http.HttpHeaders.LOCATION, locations.get(0));
                return new ResponseEntity<Void>(responseHeaders,HttpStatus.CREATED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    //Used to receive a task as JSON via HTTP POST and call the TapasTasks service to add this task via HTTP POST
    //wrapping the POST in a Hystrix command which fails after a timeout of 1 second. You can test this e.g. by enabling
    //the latency assault in TapasTasks via Chaos Monkey and then using this method to add a new task.
    @PostMapping(path = "/apptasksbreak/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewTaskCircuitBreaker(@RequestBody String payload) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Content-Type", "application/task+json")
                    .uri(URI.create("http://localhost:8081/tasks/"))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = addTaskRequest(request);

            if (response.statusCode() == 201) {
                org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
                HttpHeaders headers = response.headers();
                List<String> locations = headers.allValues("Location");
                responseHeaders.add(org.springframework.http.HttpHeaders.LOCATION, locations.get(0));
                return new ResponseEntity<Void>(responseHeaders,HttpStatus.CREATED);
            }
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    public HttpResponse addTaskRequest(HttpRequest req) {

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("addtask"));
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(1_000);
        config.andCommandPropertiesDefaults(commandProperties);

        HttpResponse resp = new HystrixCommand<HttpResponse>(HystrixCommandGroupKey.Factory.asKey("addtask")) {
            protected HttpResponse<String> run() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                return response;
            }
        }.execute();
        return resp;
    }

}
