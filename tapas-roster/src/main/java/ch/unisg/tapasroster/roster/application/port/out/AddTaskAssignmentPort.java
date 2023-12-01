package ch.unisg.tapasroster.roster.application.port.out;

import ch.unisg.tapasroster.roster.domain.TaskAssignment;

public interface AddTaskAssignmentPort {

    void addTaskAssignment(TaskAssignment taskAssignment);

}
