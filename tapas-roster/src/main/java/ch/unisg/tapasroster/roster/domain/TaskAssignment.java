package ch.unisg.tapasroster.roster.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class TaskAssignment {

    @Getter
    private final UUID taskAssignmentId;

    @Getter
    private final ExecutorBaseUri executorBaseUri;

    @Getter
    private final String taskLocation;


    public TaskAssignment(UUID taskAssignmentId, ExecutorBaseUri executorBaseUri, String taskLocation) {
        this.taskAssignmentId = taskAssignmentId;
        this.executorBaseUri = executorBaseUri;
        this.taskLocation = taskLocation;
    }

    public static TaskAssignment createAssignment(UUID taskAssignmentId, ExecutorBaseUri executorBaseUri, String taskLocation) {
        return new TaskAssignment(
            taskAssignmentId,
            executorBaseUri,
            taskLocation
        );
    }
}
