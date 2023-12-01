package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;

public interface LoadTaskPort {

    Task loadTask(Task.TaskId taskId, TaskList.TaskListName taskListName) throws TaskNotFoundError;

}
