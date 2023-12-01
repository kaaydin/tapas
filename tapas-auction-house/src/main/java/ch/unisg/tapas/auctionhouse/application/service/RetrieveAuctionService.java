package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveAuctionQuery;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveAuctionUseCase;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveOpenAuctionsUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.AuctionNotFoundError;
import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import org.springframework.stereotype.Component;

/**
 * Service that implements {@link RetrieveOpenAuctionsUseCase} to retrieve all auctions in this auction
 * house that are open for bids.
 */
@Component
public class RetrieveAuctionService implements RetrieveAuctionUseCase {

    @Override
    public Auction retrieveAuction(RetrieveAuctionQuery query) throws AuctionNotFoundError {
        return AuctionRegistry.getInstance().getAuctionById(query.getAuctionId());
    }
}
