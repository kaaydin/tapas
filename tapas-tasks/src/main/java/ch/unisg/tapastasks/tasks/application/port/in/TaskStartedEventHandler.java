package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapastasks.tasks.domain.TaskNotFoundError;

public interface TaskStartedEventHandler {

    boolean handleTaskStarted(TaskStartedEvent taskStartedEvent) throws TaskNotFoundError;
}
