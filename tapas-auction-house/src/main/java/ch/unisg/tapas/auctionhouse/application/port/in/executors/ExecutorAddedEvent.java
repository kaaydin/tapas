package ch.unisg.tapas.auctionhouse.application.port.in.executors;

import ch.unisg.tapas.auctionhouse.domain.Auction.AuctionedTaskType;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry.ExecutorIdentifier;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Event that notifies the auction house that an executor has been added to this TAPAS application.
 */
@Value
public class ExecutorAddedEvent extends SelfValidating<ExecutorAddedEvent> {
    @NotNull
    private final ExecutorIdentifier executorId;

    @NotNull
    private final AuctionedTaskType taskType;

    /**
     * Constructs an executor added event.
     *
     * @param executorId the identifier of the executor that was added to this TAPAS application
     */
    public ExecutorAddedEvent(ExecutorIdentifier executorId, AuctionedTaskType taskType) {
        this.executorId = executorId;
        this.taskType = taskType;

        this.validateSelf();
    }
}
