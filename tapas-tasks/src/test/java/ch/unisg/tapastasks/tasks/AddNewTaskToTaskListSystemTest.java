package ch.unisg.tapastasks.tasks;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.*;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="spring.profiles.active:local-mongo")
public class AddNewTaskToTaskListSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AddTaskPort addTaskPort;

    @Autowired
    private Environment environment;

    @Test
    void addNewTaskToTaskList() throws JSONException {

        Task.TaskName taskName = new Task.TaskName("system-integration-test-task");
        Task.TaskType taskType = new Task.TaskType("system-integration-test-type");

        ResponseEntity response = whenAddNewTaskToEmptyList(taskName, taskType);

        URI location = response.getHeaders().getLocation();

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody() == null);
        then(location.toString()).isNotEmpty();
        then(location.toString()).contains(environment.getProperty("baseuri"));
        then(TaskList.getTapasTaskList().getListOfTasks().getValue()).hasSize(1);

    }

    private ResponseEntity whenAddNewTaskToEmptyList(
        Task.TaskName taskName,
        Task.TaskType taskType) throws JSONException {

            TaskList.getTapasTaskList().getListOfTasks().getValue().clear();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", TaskJsonRepresentation.MEDIA_TYPE);

            String jsonPayLoad = new JSONObject()
                .put("taskName", taskName.getValue() )
                .put("taskType", taskType.getValue())
                .toString();

            HttpEntity<String> request = new HttpEntity<>(jsonPayLoad,headers);

            return restTemplate.exchange(
                "/tasks/",
                HttpMethod.POST,
                request,
                Object.class
            );

    }

}
