package ch.unisg.tapas.auctionhouse.application.service;

import org.springframework.stereotype.Component;

import ch.unisg.tapas.auctionhouse.application.port.in.bids.ReceiveBidUseCase;
import ch.unisg.tapas.auctionhouse.domain.AuctionNotFoundError;
import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import ch.unisg.tapas.auctionhouse.domain.Bid;

@Component
public class ReceiveBidService implements ReceiveBidUseCase {

    @Override
    public Boolean receiveBid(Bid bid) {
        try {
            Boolean isBidPlaced = AuctionRegistry.getInstance().placeBid(bid);
            if (isBidPlaced) {
                System.out.println("Received bid for auction " + bid.getAuctionId().getValue() + " from " + bid.getAuctionHouseUri().getValue());
                return true;
            } else {
                System.out.println("Received bid for auction " + bid.getAuctionId().getValue() + " from " + bid.getAuctionHouseUri().getValue() + " but it was already placed");
                return false;
            }
        } catch (AuctionNotFoundError auctionNotFoundError) {
            System.out.println("Received bid for non-existing or closed auction: " + bid.getAuctionId());
        }
        return false;

    }
}
