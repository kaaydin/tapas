package ch.unisg.tapas.auctionhouse.application.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import ch.unisg.tapas.auctionhouse.application.port.in.bids.ReceiveBidUseCase;
import ch.unisg.tapas.auctionhouse.domain.AuctionNotFoundError;
import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import ch.unisg.tapas.auctionhouse.domain.Bid;

@Component
public class ReceiveBidService implements ReceiveBidUseCase {
    private static final Logger LOGGER = LogManager.getLogger(ReceiveBidService.class.getName());

    @Override
    public Boolean receiveBid(Bid bid) {
        try {
            Boolean isBidPlaced = AuctionRegistry.getInstance().placeBid(bid);
            if (isBidPlaced) {
                LOGGER.info("Received bid for auction " + bid.getAuctionId().getValue() + " from " + bid.getAuctionHouseUri().getValue());
                return true;
            } else {
                LOGGER.info("Received bid for auction " + bid.getAuctionId().getValue() + " from " + bid.getAuctionHouseUri().getValue() + " but it was already placed");
                return false;
            }
        } catch (AuctionNotFoundError auctionNotFoundError) {
            LOGGER.info("Received bid for non-existing or closed auction: " + bid.getAuctionId());
        }
        return false;

    }
}
