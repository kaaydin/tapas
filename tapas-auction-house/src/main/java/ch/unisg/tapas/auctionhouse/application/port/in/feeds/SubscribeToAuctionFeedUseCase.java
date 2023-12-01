package ch.unisg.tapas.auctionhouse.application.port.in.feeds;

public interface SubscribeToAuctionFeedUseCase {

    void subscribeToFeed(SubscribeToAuctionFeedCommand command);
}
