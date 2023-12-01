package ch.unisg.tapas.auctionhouse.application.port.in.executors;

import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry.ExecutorIdentifier;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Event that notifies the auction house that an executor has been removed from this TAPAS application.
 */
@Value
public class ExecutorRemovedEvent extends SelfValidating<ExecutorRemovedEvent> {
    @NotNull
    private final ExecutorIdentifier executorId;

    /**
     * Constructs an executor removed event.
     *
     * @param executorId the identifier of the executor that was removed from this TAPAS application
     */
    public ExecutorRemovedEvent(ExecutorIdentifier executorId) {
        this.executorId = executorId;
        this.validateSelf();
    }
}
