package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapastasks.common.SelfValidating;
import ch.unisg.tapastasks.tasks.domain.Task;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class AddNewTaskToTaskListCommand extends SelfValidating<AddNewTaskToTaskListCommand> {
    @NotNull
    private final Task.TaskName taskName;

    @Getter
    private final Task.OriginalTaskUri originalTaskUri;

    @NotNull
    private final Task.TaskType taskType;

    @Getter
    private final Task.InputData taskInput;

    public AddNewTaskToTaskListCommand(Task.TaskName taskName, Task.OriginalTaskUri originalTaskUri,
            Task.TaskType taskType, Task.InputData input) {
        this.taskName = taskName;
        this.originalTaskUri = originalTaskUri;
        this.taskType = taskType;
        this.taskInput = input;

        this.validateSelf();
    }
}
