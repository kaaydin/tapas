package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.adapter.in.messaging.UnknownEventException;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;


/**
 * This REST Controller handles HTTP PATCH requests for updating the representational state of Task
 * resources. Each request to update the representational state of a Task resource can correspond to
 * at most one integration event. Request payloads use the
 * <a href="http://jsonpatch.com/">JSON PATCH</a> format and media type.
 *
 * A JSON Patch can contain multiple operations (e.g., add, remove, replace) for updating various
 * parts of a task's representations. One or more JSON Patch operations can represent an integration
 * event. Therefore, the events can only be determined by inspecting the requested patch (e.g., a request
 * to change a task's status from RUNNING to EXECUTED). This class is responsible to inspect requested
 * patches, identify events, and to route them to appropriate listeners.
 *
 * For more details on JSON Patch, see: <a href="http://jsonpatch.com/">http://jsonpatch.com/</a>
 * For some sample HTTP requests, see the README.
 */
@RestController
@RequiredArgsConstructor
public class TaskEventHttpDispatcher {
    // Used to retrieve properties from application.properties
    @Autowired
    private Environment environment;

    private final TaskAssignedEventListenerHttpAdapter taskAssignedEventListenerHttpAdapter;
    private final TaskStartedEventListenerHttpAdapter taskStartedEventListenerHttpAdapter;
    private final TaskExecutedEventListenerHttpAdapter taskExecutedEventListenerHttpAdapter;

    // The standard media type for JSON Patch registered with IANA
    // See: https://www.iana.org/assignments/media-types/application/json-patch+json
    private final static String JSON_PATCH_MEDIA_TYPE = "application/json-patch+json";

    /**
     * Handles HTTP PATCH requests with a JSON Patch payload. Routes the requests based on
     * the operations requested in the patch. In this implementation, one HTTP Patch request is
     * mapped to at most one domain event.
     *
     * @param taskId  the local (i.e., implementation-specific) identifier of the task to the patched;
     *                this identifier is extracted from the task's URI
     * @param payload the requested patch for this task
     * @return 200 OK and boolean true after processing the event; 404 Not Found if
     * the request URI does not match any task; 400 Bad Request if the request is invalid
     */
    @PatchMapping(path = "/tasks/{taskId}", consumes = {JSON_PATCH_MEDIA_TYPE})
    public ResponseEntity<Void> dispatchTaskEvents(@PathVariable("taskId") String taskId,
                                                   @RequestBody JsonNode payload) {
        try {
            // Throw an exception if the JSON Patch format is invalid. This call is only used to
            // validate the JSON PATCH syntax.
            JsonPatch.fromJson(payload);

            // Check for known events and route the events to appropriate listeners
            TaskJsonPatchRepresentation representation = new TaskJsonPatchRepresentation(payload);
            Optional<Task.Status> status = representation.extractFirstTaskStatusChange();

            TaskEventListener listener = null;

            // Route events related to task status changes
            if (status.isPresent()) {
                switch (status.get()) {
                    case ASSIGNED -> listener = taskAssignedEventListenerHttpAdapter;
                    case RUNNING -> listener = taskStartedEventListenerHttpAdapter;
                    case EXECUTED -> listener = taskExecutedEventListenerHttpAdapter;
                }
            }

            if (listener == null) {
                // The HTTP PATCH request is valid, but the patch does not match any known event
                throw new UnknownEventException();
            }

            listener.handleTaskEvent(taskId, payload);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.LOCATION, environment.getProperty("baseuri")
                + "tasks/" + taskId);
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } catch (TaskNotFoundError e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
