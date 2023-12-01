package ch.unisg.tapasroster.roster;


import ch.unisg.tapasroster.roster.adapter.in.formats.TaskJsonRepresentation;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active:local-mongo")
public class AssignTaskToExecutorSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void assignNewTaskToExecutor() throws JSONException {

        String taskType = "CALCULATING";
        String taskListName = "system-integration-test-list";
        String taskLocation = "system-integration-test-location";

        ResponseEntity<?> assignNewTaskToExecutorResponse = whenAssignNewTaskToExecutor(taskLocation, taskListName, taskType);

        //Asserts if HTTP request returns 200; since the test does not find an executor to execute the task, the request body is not checked
        then(assignNewTaskToExecutorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private ResponseEntity<?> whenAssignNewTaskToExecutor(
        String taskLocation,
        String taskListName,
        String taskType) throws JSONException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", TaskJsonRepresentation.MEDIA_TYPE);

        String jsonPayLoad = new JSONObject()
            .put("taskLocation", taskLocation)
            .put("taskListName", taskListName)
            .put("taskType", taskType)
            .toString();

        HttpEntity<String> request = new HttpEntity<>(jsonPayLoad, headers);

        return restTemplate.exchange("/roster/newtask/", HttpMethod.POST, request, String.class);
    }

}
