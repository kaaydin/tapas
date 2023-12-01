package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;

public class NewTaskAddedEvent {
    public Task.TaskId taskId;
    public TaskList.TaskListName taskListName;
    public Task.TaskType taskType;

    public NewTaskAddedEvent(Task.TaskId taskId, TaskList.TaskListName taskListName, Task.TaskType taskType) {
        this.taskId = taskId;
        this.taskListName = taskListName;
        this.taskType = taskType;
    }
}

