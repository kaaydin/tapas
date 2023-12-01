package ch.unisg.tapasroster.roster.application.port.out;

import ch.unisg.tapasroster.roster.domain.TaskAssignment;

import java.util.List;

public interface LoadTaskAssignmentsPort {

    List<TaskAssignment>  loadTaskAssignments();

}
