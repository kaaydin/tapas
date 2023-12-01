package ch.unisg.tapas.auctionhouse.application.port.out.bids;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;

/**
 * Port for placing a bid in an auction
 */
public interface PlaceBidForAuctionPort {

    void placeBid(Auction auction, Bid bid);
}
