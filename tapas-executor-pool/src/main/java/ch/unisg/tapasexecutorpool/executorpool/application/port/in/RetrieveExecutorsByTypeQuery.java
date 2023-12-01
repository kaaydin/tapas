package ch.unisg.tapasexecutorpool.executorpool.application.port.in;

import ch.unisg.tapasexecutorpool.common.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public class RetrieveExecutorsByTypeQuery extends SelfValidating<RetrieveExecutorsByTypeQuery> {

    @NotNull
    private final String executorType;

    public RetrieveExecutorsByTypeQuery(String executorType) {
        this.executorType = executorType;
        this.validateSelf();
    }
}
