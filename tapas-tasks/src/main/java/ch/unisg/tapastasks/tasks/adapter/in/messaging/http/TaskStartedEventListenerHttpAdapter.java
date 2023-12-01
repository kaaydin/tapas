package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEventHandler;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.Task.TaskId;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Listener for task started events. A task started event corresponds to a JSON Patch that attempts
 * to change the task's status to RUNNING.
 *
 * See also {@link TaskStartedEvent}, {@link Task}, and {@link TaskEventHttpDispatcher}.
 */
@Component
@RequiredArgsConstructor
public class TaskStartedEventListenerHttpAdapter extends TaskEventListener {

    private final TaskStartedEventHandler taskStartedEventHandler;

    /**
     * Handles the task started event.
     *
     * @param taskId the identifier of the task for which an event was received
     * @param payload the JSON Patch payload of the HTTP PATCH request received for this task
     * @return true if the task started event was handled successfully, false otherwise
     */
    public boolean handleTaskEvent(String taskId, JsonNode payload) throws TaskNotFoundError {
        TaskJsonPatchRepresentation representation = new TaskJsonPatchRepresentation(payload);
        Optional<Task.ServiceProvider> serviceProvider = representation.extractFirstServiceProviderChange();

        TaskStartedEvent taskStartedEvent = new TaskStartedEvent(new TaskId(taskId), serviceProvider.orElse(null));

        return taskStartedEventHandler.handleTaskStarted(taskStartedEvent);
    }
}
