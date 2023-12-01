package ch.unisg.tapas.auctionhouse.application.port.in.bids;

import ch.unisg.tapas.auctionhouse.domain.Bid;

/**
 * Port for receiving bids from other auction houses
 */
public interface ReceiveBidUseCase {

    Boolean receiveBid(Bid bid);
}
