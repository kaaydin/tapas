package ch.unisg.tapastasks.tasks.application.handler;

import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEventHandler;
import ch.unisg.tapastasks.tasks.application.port.out.UpdateTaskPort;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskAssignedHandler implements TaskAssignedEventHandler {

    private final UpdateTaskPort updateTaskInRepositoryPort;

    @Override
    public boolean handleTaskAssigned(TaskAssignedEvent taskAssignedEvent) throws TaskNotFoundError {
        TaskList taskList = TaskList.getTapasTaskList();
        boolean result = taskList.changeTaskStatusToAssigned(taskAssignedEvent.getTaskId(),
            taskAssignedEvent.getServiceProvider());

        if (result) {
            Task task = taskList.retrieveTaskById(taskAssignedEvent.getTaskId());
            updateTaskInRepositoryPort.updateTask(task);
        }

        return result;
    }
}
