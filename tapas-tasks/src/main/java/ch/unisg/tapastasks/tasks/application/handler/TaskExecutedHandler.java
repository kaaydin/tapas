package ch.unisg.tapastasks.tasks.application.handler;

import ch.unisg.tapastasks.tasks.application.port.in.TaskExecutedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskExecutedEventHandler;
import ch.unisg.tapastasks.tasks.application.port.out.UpdateTaskPort;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskExecutedHandler implements TaskExecutedEventHandler {

    private final UpdateTaskPort updateTaskInRepositoryPort;

    @Override
    public boolean handleTaskExecuted(TaskExecutedEvent taskExecutedEvent) throws TaskNotFoundError {
        TaskList taskList = TaskList.getTapasTaskList();
        boolean result = taskList.changeTaskStatusToExecuted(taskExecutedEvent.getTaskId(),
            taskExecutedEvent.getServiceProvider(), taskExecutedEvent.getOutputData());

        if (result) {
            Task task = taskList.retrieveTaskById(taskExecutedEvent.getTaskId());
            updateTaskInRepositoryPort.updateTask(task);
        }

        return result;
    }
}
