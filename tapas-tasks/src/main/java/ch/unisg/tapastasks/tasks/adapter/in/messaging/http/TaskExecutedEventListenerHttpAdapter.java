package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.TaskExecutedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskExecutedEventHandler;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Listener for task executed events. A task executed event corresponds to a JSON Patch that attempts
 * to change the task's status to EXECUTED, and may also add an
 * output result.
 *
 * See also {@link TaskExecutedEvent}, {@link Task}, and {@link TaskEventHttpDispatcher}.
 */
@Component
@RequiredArgsConstructor
public class TaskExecutedEventListenerHttpAdapter extends TaskEventListener {

    private final TaskExecutedEventHandler taskExecutedEventHandler;

    /**
     * Handles the task executed event.
     *
     * @param taskId the identifier of the task for which an event was received
     * @param payload the JSON Patch payload of the HTTP PATCH request received for this task
     * @return true if the task executed event was handled successfully, false otherwise
     */
    public boolean handleTaskEvent(String taskId, JsonNode payload) throws TaskNotFoundError {
        TaskJsonPatchRepresentation representation = new TaskJsonPatchRepresentation(payload);

        Optional<Task.ServiceProvider> serviceProvider = representation.extractFirstServiceProviderChange();
        Optional<Task.OutputData> outputData = representation.extractFirstOutputDataAddition();

        TaskExecutedEvent taskExecutedEvent = new TaskExecutedEvent(new Task.TaskId(taskId),
            serviceProvider.orElse(null), outputData.orElse(null));

        return taskExecutedEventHandler.handleTaskExecuted(taskExecutedEvent);
    }
}
