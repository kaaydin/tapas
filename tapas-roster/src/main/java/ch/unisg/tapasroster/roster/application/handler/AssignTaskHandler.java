package ch.unisg.tapasroster.roster.application.handler;

import ch.unisg.tapasroster.roster.adapter.out.web.StartExecutorWebAdapter;
import ch.unisg.tapasroster.roster.application.port.out.StartExecutorPort;
import ch.unisg.tapasroster.roster.application.port.out.AddTaskAssignmentPort;
import ch.unisg.tapasroster.roster.application.port.in.AssignTaskEvent;
import ch.unisg.tapasroster.roster.application.port.in.AssignTaskEventHandler;
import ch.unisg.tapasroster.roster.application.port.out.FindExecutorPort;
import ch.unisg.tapasroster.roster.application.port.out.StartAuctionPort;
import ch.unisg.tapasroster.roster.domain.*;


import ch.unisg.tapasroster.roster.domain.TaskAssignmentList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssignTaskHandler implements AssignTaskEventHandler {
    private final AddTaskAssignmentPort addTaskAssignmentPort;
    private final FindExecutorPort findExecutorPort;
    private final StartExecutorPort startExecutorPort;
    private final StartAuctionPort startAuctionPort;
    private static final Logger LOGGER = LogManager.getLogger(StartExecutorWebAdapter.class.getName());

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
            LOGGER.info("Assigning task " + event.getTaskLocation() + " to executor " + executorBaseUri.getBaseUri());
            // Create new task assignment
            TaskAssignment taskAssignment = new TaskAssignment(UUID.randomUUID(), executorBaseUri, event.getTaskLocation());
            taskAssignmentList.addNewAssignment(taskAssignment);
            addTaskAssignmentPort.addTaskAssignment(taskAssignment);
            //Execute task
            startExecutorPort.startExecutor(executorBaseUri, event.getTaskLocation());

            return taskAssignment;
        } else {
            LOGGER.info("No internal executor found, starting auction");
            startAuctionPort.startAuction(event.getTaskLocation(), event.getTaskType());
            return null;
        }
    }
}
