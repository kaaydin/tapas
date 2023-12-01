package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapastasks.tasks.domain.Task;

public interface AddTaskPort {

    void addTask(Task task);

}
