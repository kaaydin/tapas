package ch.unisg.tapastasks.tasks.adapter.in.web;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListUseCase;
import ch.unisg.tapastasks.tasks.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

/**
 * Controller that handles HTTP requests for creating new tasks. This controller implements the
 * {@link AddNewTaskToTaskListUseCase} use case using the {@link AddNewTaskToTaskListCommand}.
 *
 * A new task is created via an HTTP POST request to the /tasks/ endpoint.
 *
 * If the request is successful, the controller returns an HTTP 201 Created status code a Location header field that
 * points to the URI of the created task.
 */

@RestController
@RequiredArgsConstructor
public class AddNewTaskToTaskListWebController {

    //Instead of explicitly adding an ingoing port here, we are directly referencing the use case to reduce redundancy.
    private final AddNewTaskToTaskListUseCase addNewTaskToTaskListUseCase;

    // Used to retrieve properties from application.properties
    @Autowired
    private Environment environment;

    @PostMapping(path = "/tasks/", consumes = {TaskJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<Void> addNewTaskTaskToTaskList(@RequestBody TaskJsonRepresentation payload) {
        try {
            Task.TaskName taskName = new Task.TaskName(payload.getTaskName());
            Task.TaskType taskType = new Task.TaskType(payload.getTaskType());

            // If the created task is a delegated task, the representation contains a URI reference
            // to the original task
            Task.OriginalTaskUri originalTaskUri =
                (payload.getOriginalTaskUri() == null) ? null : new Task.OriginalTaskUri(payload.getOriginalTaskUri());

            // When creating a task, the task's representation may include optional input data
            Task.InputData taskInputData =
                (payload.getInputData() == null) ? null : new Task.InputData(payload.getInputData());

            AddNewTaskToTaskListCommand command = new AddNewTaskToTaskListCommand(taskName, originalTaskUri,
                taskType, taskInputData);

            String taskId = addNewTaskToTaskListUseCase.addNewTaskToTaskList(command);

            HttpHeaders responseHeaders = new HttpHeaders();
            // Construct and advertise the URI of the newly created task; we retrieve the base URI
            // from the application.properties file
            responseHeaders.add(HttpHeaders.LOCATION, environment.getProperty("baseuri")
                + "tasks/" + taskId);

            // We do not send a body here since we only executed a command to create a new task and not to retrieve it,
            // which corresponds to the command-query-separation.
            // https://blog.ploeh.dk/2014/08/11/cqs-versus-server-generated-ids/
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
