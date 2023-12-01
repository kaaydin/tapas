package ch.unisg.tapas.auctionhouse.application.port.out.auctions;

import ch.unisg.tapas.auctionhouse.domain.AuctionWonEvent;

/**
 * Port for sending out auction won events
 */
public interface AuctionWonEventPort {

    void publishAuctionWonEvent(AuctionWonEvent event);
}
