package ch.unisg.tapastasks.tasks.application.handler;

import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEventHandler;
import ch.unisg.tapastasks.tasks.application.port.out.UpdateTaskPort;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskStartedHandler implements TaskStartedEventHandler {

    private final UpdateTaskPort updateTaskInRepositoryPort;

    @Override
    public boolean handleTaskStarted(TaskStartedEvent taskStartedEvent) throws TaskNotFoundError {
        TaskList taskList = TaskList.getTapasTaskList();
        boolean result = taskList.changeTaskStatusToRunning(taskStartedEvent.getTaskId(),
            taskStartedEvent.getServiceProvider());

        if (result) {
            Task task = taskList.retrieveTaskById(taskStartedEvent.getTaskId());
            updateTaskInRepositoryPort.updateTask(task);
        }

        return result;
    }
}
