package ch.unisg.tapas.auctionhouse.application.port.in.auctions;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Command for launching an auction in this auction house.
 */
@Value
public class LaunchAuctionCommand extends SelfValidating<LaunchAuctionCommand> {
    @NotNull
    private final Auction.AuctionedTaskUri taskUri;

    @NotNull
    private final Auction.AuctionedTaskType taskType;

    private final Auction.AuctionDeadline deadline;

    private final Auction.InputData inputData;

    /**
     * Constructs the launch auction command.
     *
     * @param taskUri   the URI of the auctioned task
     * @param taskType  the type of the auctioned task
     * @param deadline  the deadline by which the auction should receive bids (can be null if none)
     * @param inputData
     */
    public LaunchAuctionCommand(Auction.AuctionedTaskUri taskUri, Auction.AuctionedTaskType taskType,
                                Auction.AuctionDeadline deadline, Auction.InputData inputData) {
        this.taskUri = taskUri;
        this.taskType = taskType;
        this.deadline = deadline;
        this.inputData = inputData;

        this.validateSelf();
    }
}
