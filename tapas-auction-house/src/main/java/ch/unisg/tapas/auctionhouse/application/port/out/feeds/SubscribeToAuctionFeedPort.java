package ch.unisg.tapas.auctionhouse.application.port.out.feeds;

import ch.unisg.tapas.auctionhouse.domain.Auction;

/**
 * Port for subscribing to an auction feed
 */
public interface SubscribeToAuctionFeedPort {

    void subscribeToFeed(Auction.AuctionFeedId feedId);
}
