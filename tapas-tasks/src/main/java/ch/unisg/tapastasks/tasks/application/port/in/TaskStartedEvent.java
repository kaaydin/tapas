package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapastasks.common.SelfValidating;
import ch.unisg.tapastasks.tasks.domain.Task;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class TaskStartedEvent extends SelfValidating<TaskStartedEvent> {
    @NotNull
    private final Task.TaskId taskId;

    @Getter
    private final Task.ServiceProvider serviceProvider;

    public TaskStartedEvent(Task.TaskId taskId, Task.ServiceProvider serviceProvider) {
        this.taskId = taskId;
        this.serviceProvider = serviceProvider;

        this.validateSelf();
    }



}
