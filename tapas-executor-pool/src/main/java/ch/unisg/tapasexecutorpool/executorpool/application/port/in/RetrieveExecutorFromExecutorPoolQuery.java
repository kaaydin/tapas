package ch.unisg.tapasexecutorpool.executorpool.application.port.in;

import ch.unisg.tapasexecutorpool.common.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public class RetrieveExecutorFromExecutorPoolQuery extends SelfValidating<RetrieveExecutorFromExecutorPoolQuery> {
    @NotNull
    private final String executorId;

    public RetrieveExecutorFromExecutorPoolQuery(String executorId) {
        this.executorId = executorId;
        this.validateSelf();
    }
}
