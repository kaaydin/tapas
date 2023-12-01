package ch.unisg.tapas.auctionhouse.application.port.in.auctions;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Event that notifies this auction house that an auction was started by another auction house.
 */
@Value
public class AuctionStartedEvent extends SelfValidating<AuctionStartedEvent> {
    @NotNull
    private final Auction auction;

    public AuctionStartedEvent(Auction auction) {
        this.auction = auction;
        this.validateSelf();
    }
}
