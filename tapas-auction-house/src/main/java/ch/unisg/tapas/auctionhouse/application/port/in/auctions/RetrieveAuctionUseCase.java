package ch.unisg.tapas.auctionhouse.application.port.in.auctions;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.AuctionNotFoundError;

public interface RetrieveAuctionUseCase {

    Auction retrieveAuction(RetrieveAuctionQuery query) throws AuctionNotFoundError;
}
