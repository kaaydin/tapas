package ch.unisg.tapasexecutorpool.executorpool.application.port.in;

import ch.unisg.tapasexecutorpool.common.SelfValidating;

import lombok.Getter;
import lombok.Value;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper=false)
public class AddNewExecutorToExecutorPoolCommand extends SelfValidating<AddNewExecutorToExecutorPoolCommand> {
    @NotNull
    private final String executorName;

    @NotNull
    private final String executorType;

    @Getter
    private final String executorBaseUri;

    public AddNewExecutorToExecutorPoolCommand(String executorName, String executorType, String executorBaseUri) {
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorBaseUri = executorBaseUri;

        this.validateSelf();
    }
}

