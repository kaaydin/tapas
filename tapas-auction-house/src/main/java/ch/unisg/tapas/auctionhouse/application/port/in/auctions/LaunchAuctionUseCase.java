package ch.unisg.tapas.auctionhouse.application.port.in.auctions;

import ch.unisg.tapas.auctionhouse.domain.Auction;

public interface LaunchAuctionUseCase {

    Auction.AuctionId launchAuction(LaunchAuctionCommand command);
}
