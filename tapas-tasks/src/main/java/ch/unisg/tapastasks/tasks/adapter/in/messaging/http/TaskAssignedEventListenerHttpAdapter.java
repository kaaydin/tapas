package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEventHandler;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.Task.TaskId;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Listener for task assigned events. A task assigned event corresponds to a JSON Patch that attempts
 * to change the task's status to ASSIGNED and may also add/replace a service provider (i.e., to what
 * group the task was assigned). This implementation does not impose that a task assigned event
 * includes the service provider (i.e., can be null).
 *
 * See also {@link TaskAssignedEvent}, {@link Task}, and {@link TaskEventHttpDispatcher}.
 */
@Component
@RequiredArgsConstructor
public class TaskAssignedEventListenerHttpAdapter extends TaskEventListener {

    private final TaskAssignedEventHandler taskAssignedEventHandler;

    /**
     * Handles the task assigned event.
     *
     * @param taskId the identifier of the task for which an event was received
     * @param payload the JSON Patch payload of the HTTP PATCH request received for this task
     * @return true if the task assigned event was handled successfully, false otherwise
     */
    public boolean handleTaskEvent(String taskId, JsonNode payload) throws TaskNotFoundError {
        TaskJsonPatchRepresentation representation = new TaskJsonPatchRepresentation(payload);
        Optional<Task.ServiceProvider> serviceProvider = representation.extractFirstServiceProviderChange();

        TaskAssignedEvent taskAssignedEvent = new TaskAssignedEvent(new TaskId(taskId), serviceProvider.orElse(null));

        return taskAssignedEventHandler.handleTaskAssigned(taskAssignedEvent);
    }
}
