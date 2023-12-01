package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveOpenAuctionsQuery;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveOpenAuctionsUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service that implements {@link RetrieveOpenAuctionsUseCase} to retrieve all auctions in this auction
 * house that are open for bids.
 */
@Component
public class RetrieveOpenAuctionsService implements RetrieveOpenAuctionsUseCase {

    @Override
    public Collection<Auction> retrieveAuctions(RetrieveOpenAuctionsQuery query) {
        return AuctionRegistry.getInstance().getOpenAuctions();
    }
}
