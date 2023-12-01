package ch.unisg.tapas.auctionhouse.application.handler;

import ch.unisg.tapas.auctionhouse.application.port.in.auctions.AuctionStartedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.AuctionStartedEventHandler;
import ch.unisg.tapas.auctionhouse.application.port.out.bids.PlaceBidForAuctionPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import ch.unisg.tapas.common.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handler for auction started events. This handler will automatically bid in any auction for a
 * task of known type, i.e. a task for which the auction house knows an executor is available.
 */
@Component
public class AuctionStartedHandler implements AuctionStartedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger(AuctionStartedHandler.class);

    @Autowired
    private ConfigProperties config;

    @Autowired
    private PlaceBidForAuctionPort placeBidForAuctionPort;

    /**
     * Handles an auction started event and bids in all auctions for tasks of known types.
     *
     * @param auctionStartedEvent the auction started domain event
     * @return true unless a runtime exception occurs
     */
    @Override
    public boolean handleAuctionStartedEvent(AuctionStartedEvent auctionStartedEvent) {
        Auction auction = auctionStartedEvent.getAuction();

        if (ExecutorRegistry.getInstance().containsTaskType(auction.getTaskType())) {
            LOGGER.info("Placing bid for task " + auction.getTaskUri().getValue() + " of type "
                + auction.getTaskType().getValue() + " in auction " + auction.getAuctionId().getValue()
                + " from auction house " + auction.getAuctionHouseUri().getValue().toString());

            Bid bid = new Bid(auction.getAuctionId(),
                new Bid.BidderName(config.getGroupName()),
                new Bid.AuctionHouseUri(config.getAuctionHouseUri()),
                new Bid.TaskListUri(config.getTaskListUri())
            );

            placeBidForAuctionPort.placeBid(auction, bid);
        } else {
            LOGGER.info("Cannot execute this task type: " + auction.getTaskType().getValue());
        }

        return true;
    }
}
