package ch.unisg.tapas.auctionhouse.application.port.in.auctions;

public interface AuctionStartedEventHandler {

    boolean handleAuctionStartedEvent(AuctionStartedEvent auctionStartedEvent);
}
