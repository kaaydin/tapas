package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListQuery;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListUseCase;
import ch.unisg.tapastasks.tasks.application.port.out.LoadTaskPort;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("RetrieveTaskFromTaskList")
public class RetrieveTaskFromTaskListService implements RetrieveTaskFromTaskListUseCase {

    private final LoadTaskPort loadTaskFromRepositoryPort;

    @Override
    public Task retrieveTaskFromTaskList(RetrieveTaskFromTaskListQuery query) throws TaskNotFoundError {
        TaskList taskList = TaskList.getTapasTaskList();

        // Check if it is in the task list in memory
        try {
            Task task = taskList.retrieveTaskById(query.getTaskId());
            return task;
        } catch (TaskNotFoundError e) {
            // Check if it is in the repo instead (not loaded) and add to current task list
            Task task = loadTaskFromRepositoryPort.loadTask(query.getTaskId(), taskList.getTaskListName());
            taskList.addExistingTaskToTaskList(task);
            return task;
        }
    }
}
