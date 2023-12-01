package ch.unisg.tapasroster.roster.application.handler;

import ch.unisg.tapasroster.roster.application.port.out.StartExecutorPort;
import ch.unisg.tapasroster.roster.application.port.out.AddTaskAssignmentPort;
import ch.unisg.tapasroster.roster.application.port.in.AssignTaskEvent;
import ch.unisg.tapasroster.roster.application.port.in.AssignTaskEventHandler;
import ch.unisg.tapasroster.roster.application.port.out.FindExecutorPort;
import ch.unisg.tapasroster.roster.application.port.out.StartAuctionPort;
import ch.unisg.tapasroster.roster.domain.TaskAssignment;
import ch.unisg.tapasroster.roster.domain.TaskAssignmentList;
import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;


import ch.unisg.tapasroster.roster.domain.TaskAssignmentList;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AssignTaskHandler implements AssignTaskEventHandler {
    private final AddTaskAssignmentPort addTaskAssignmentPort;
    private final FindExecutorPort findExecutorPort;
    private final StartExecutorPort startExecutorPort;
    private final StartAuctionPort startAuctionPort;

    public AssignTaskHandler(FindExecutorPort findExecutorPort, StartExecutorPort startExecutorPort, AddTaskAssignmentPort addTaskAssignmentPort, StartAuctionPort startAuctionPort) {
        this.findExecutorPort = findExecutorPort;
        this.startExecutorPort = startExecutorPort;
        this.addTaskAssignmentPort = addTaskAssignmentPort;
        this.startAuctionPort = startAuctionPort;
    }

    @Override
    public TaskAssignment assignTaskToExecutor(AssignTaskEvent event) {
        TaskAssignmentList taskAssignmentList = TaskAssignmentList.getTaskAssignmentList();

        // Find available executor in executor pool
        ExecutorBaseUri executorBaseUri = findExecutorPort.findExecutor(event.getTaskType());
        if (executorBaseUri != null) {
            System.out.println("Assigning task " + event.getTaskLocation() + " to executor " + executorBaseUri.getBaseUri());
            //Execute task
            startExecutorPort.startExecutor(executorBaseUri, event.getTaskLocation());
        } else {
            //create pseudo executor baseuri
            executorBaseUri = new ExecutorBaseUri(null);

            startAuctionPort.startAuction(event.getTaskLocation(), event.getTaskType());
        }
        
        // Create new task assignment
        TaskAssignment taskAssignment = new TaskAssignment(UUID.randomUUID(), executorBaseUri, event.getTaskLocation());
        // Add to assignment list

        taskAssignmentList.addNewAssignment(taskAssignment);
        addTaskAssignmentPort.addTaskAssignment(taskAssignment);
        return taskAssignment;
    }
}
