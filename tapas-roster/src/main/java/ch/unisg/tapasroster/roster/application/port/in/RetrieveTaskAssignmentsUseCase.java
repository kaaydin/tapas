package ch.unisg.tapasroster.roster.application.port.in;

import ch.unisg.tapasroster.roster.domain.TaskAssignment;

import java.util.List;
import java.util.Optional;

public interface RetrieveTaskAssignmentsUseCase {
    List<TaskAssignment> retrieveTaskAssignments(RetrieveTaskAssignmentsQuery query);
}
