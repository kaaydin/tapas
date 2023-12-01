package ch.unisg.tapasroster.roster.application.port.in;

import ch.unisg.tapasroster.roster.domain.TaskAssignment;

public interface AssignTaskEventHandler {
    TaskAssignment assignTaskToExecutor(AssignTaskEvent assignTaskEvent);
}
