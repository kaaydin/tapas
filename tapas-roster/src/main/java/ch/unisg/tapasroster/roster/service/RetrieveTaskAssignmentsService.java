package ch.unisg.tapasroster.roster.service;

import ch.unisg.tapasroster.roster.application.port.in.RetrieveTaskAssignmentsUseCase;
import ch.unisg.tapasroster.roster.domain.TaskAssignment;
import ch.unisg.tapasroster.roster.domain.TaskAssignmentList;
import ch.unisg.tapasroster.roster.application.port.out.LoadTaskAssignmentsPort;

import ch.unisg.tapasroster.roster.application.port.in.RetrieveTaskAssignmentsQuery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service("RetrieveAllAssignments")
public class RetrieveTaskAssignmentsService implements RetrieveTaskAssignmentsUseCase {

    private final LoadTaskAssignmentsPort loadTaskAssignmentsPort;

    @Override
    public List<TaskAssignment> retrieveTaskAssignments(RetrieveTaskAssignmentsQuery query) {
        List<TaskAssignment> taskAssignmentList = TaskAssignmentList.retrieveAllAssignments();
        if (taskAssignmentList.isEmpty()){
            return loadTaskAssignmentsPort.loadTaskAssignments();
        }
        return taskAssignmentList;

    }
}
