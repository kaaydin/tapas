package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapastasks.common.SelfValidating;
import ch.unisg.tapastasks.tasks.domain.Task.*;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class TaskExecutedEvent extends SelfValidating<TaskExecutedEvent> {
    @NotNull
    private final TaskId taskId;

    @Getter
    private final ServiceProvider serviceProvider;

    @Getter
    private final OutputData outputData;

    public TaskExecutedEvent(TaskId taskId, ServiceProvider serviceProvider, OutputData outputData) {
        this.taskId = taskId;
        this.serviceProvider = serviceProvider;
        this.outputData = outputData;

        this.validateSelf();
    }



}
