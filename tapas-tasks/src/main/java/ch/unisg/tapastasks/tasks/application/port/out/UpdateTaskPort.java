package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapastasks.tasks.domain.Task;

public interface UpdateTaskPort {

    void updateTask(Task task);

}
