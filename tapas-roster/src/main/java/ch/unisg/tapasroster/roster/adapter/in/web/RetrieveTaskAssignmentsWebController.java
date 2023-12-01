package ch.unisg.tapasroster.roster.adapter.in.web;

import ch.unisg.tapasroster.roster.application.port.in.RetrieveTaskAssignmentsQuery;
import ch.unisg.tapasroster.roster.application.port.in.RetrieveTaskAssignmentsUseCase;
import ch.unisg.tapasroster.roster.domain.TaskAssignment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RetrieveTaskAssignmentsWebController {

    private final RetrieveTaskAssignmentsUseCase retrieveTaskAssignmentUseCase;

    @GetMapping(path = "/roster")
    public List<TaskAssignment> retrieveAllTaskAssignments() {
        RetrieveTaskAssignmentsQuery query = new RetrieveTaskAssignmentsQuery();
        List<TaskAssignment> listOfAllAssignments = retrieveTaskAssignmentUseCase.retrieveTaskAssignments(query);
        return listOfAllAssignments;
    }
}
