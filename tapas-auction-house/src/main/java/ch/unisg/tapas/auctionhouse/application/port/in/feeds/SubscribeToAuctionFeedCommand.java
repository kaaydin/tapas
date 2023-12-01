package ch.unisg.tapas.auctionhouse.application.port.in.feeds;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Command for subscribing to auction feeds discovered via the Auction House Resource Directory
 */
@Value
public class SubscribeToAuctionFeedCommand extends SelfValidating<SubscribeToAuctionFeedCommand> {
    @NotNull
    private final Auction.AuctionFeedId feedId;

    public SubscribeToAuctionFeedCommand(Auction.AuctionFeedId feedId) {
        this.feedId = feedId;
        this.validateSelf();
    }
}
