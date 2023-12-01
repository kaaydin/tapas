package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapastasks.tasks.domain.TaskList;

public interface TaskListLock {

    void lockTaskList(TaskList.TaskListName taskListName);

    void releaseTaskList(TaskList.TaskListName taskListName);

}
