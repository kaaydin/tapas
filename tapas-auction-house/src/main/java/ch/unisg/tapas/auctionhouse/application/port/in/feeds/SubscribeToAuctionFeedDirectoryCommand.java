package ch.unisg.tapas.auctionhouse.application.port.in.feeds;

import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.net.URI;

/**
 * Command for subscribing to auction feeds discovered via the Auction House Resource Directory
 */
@Value
public class SubscribeToAuctionFeedDirectoryCommand extends SelfValidating<SubscribeToAuctionFeedDirectoryCommand> {
    @NotNull
    private final URI feedId;

    public SubscribeToAuctionFeedDirectoryCommand(URI feedId) {
        this.feedId = feedId;
        this.validateSelf();
    }
}
