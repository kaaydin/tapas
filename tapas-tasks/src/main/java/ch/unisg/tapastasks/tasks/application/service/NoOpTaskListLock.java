package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import org.springframework.stereotype.Component;

@Component
public class NoOpTaskListLock implements TaskListLock {
    @Override
    public void lockTaskList(TaskList.TaskListName taskListName) {
        //do nothing
    }

    @Override
    public void releaseTaskList(TaskList.TaskListName taskListName) {
        //do nothing
    }
}
