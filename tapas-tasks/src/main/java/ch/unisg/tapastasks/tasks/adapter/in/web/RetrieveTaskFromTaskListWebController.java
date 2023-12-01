package ch.unisg.tapastasks.tasks.adapter.in.web;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListQuery;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListUseCase;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller that handles HTTP GET requests for retrieving tasks. This controller implements the
 * {@link RetrieveTaskFromTaskListUseCase} use case using the {@link RetrieveTaskFromTaskListQuery}
 * query.
 */
@RestController
@RequiredArgsConstructor
public class RetrieveTaskFromTaskListWebController {
    private final RetrieveTaskFromTaskListUseCase retrieveTaskFromTaskListUseCase;

    /**
     * Retrieves a representation of task. Returns HTTP 200 OK if the request is successful. The body of the request
     * contains a JSON-based representation with the "application/task+json" media type defined for this
     * project. This custom media type allows to capture the semantics of our JSON representations for
     * tasks.
     *
     * @param taskId the local identifier of the requested task (extracted from the task's URI)
     * @return a representation of the task if the task exists
     */
    @GetMapping(path = "/tasks/{taskId}")
    public ResponseEntity<String> retrieveTaskFromTaskList(@PathVariable("taskId") String taskId) {
        RetrieveTaskFromTaskListQuery query = new RetrieveTaskFromTaskListQuery(new Task.TaskId(taskId));
        try {
            Task retrievedTask = retrieveTaskFromTaskListUseCase.retrieveTaskFromTaskList(query);
            String taskRepresentation = TaskJsonRepresentation.serialize(retrievedTask);

            // Add the content type as a response header
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE);

            return new ResponseEntity<>(taskRepresentation, responseHeaders, HttpStatus.OK);
        } catch (TaskNotFoundError te) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
