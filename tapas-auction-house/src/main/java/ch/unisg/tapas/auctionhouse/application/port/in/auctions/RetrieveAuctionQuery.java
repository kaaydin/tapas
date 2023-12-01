package ch.unisg.tapas.auctionhouse.application.port.in.auctions;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Query used to retrieve an auction.
 */
@Value
public class RetrieveAuctionQuery extends SelfValidating<RetrieveAuctionQuery> {
    @NotNull
    private final Auction.AuctionId auctionId;

    public RetrieveAuctionQuery(Auction.AuctionId auctionId) {
        this.auctionId = auctionId;
        this.validateSelf();
    }
}
