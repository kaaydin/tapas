package ch.unisg.tapasroster.roster.application.port.in;

import ch.unisg.tapasroster.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = false)

public class AssignTaskEvent extends SelfValidating<AssignTaskEvent> {

    @NotNull
    String taskLocation;

    @NotNull
    String taskListName;

    @NotNull
    String taskType;

    public AssignTaskEvent(String taskLocation, String taskListName, String taskType) {
        this.taskLocation = taskLocation;
        this.taskListName = taskListName;
        this.taskType = taskType;
        this.validateSelf();
    }
}
