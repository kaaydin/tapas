package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;

public interface TaskAssignedEventHandler {

    boolean handleTaskAssigned(TaskAssignedEvent taskStartedEvent) throws TaskNotFoundError;
}
