package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.auctions.LaunchAuctionCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.LaunchAuctionUseCase;
import ch.unisg.tapas.auctionhouse.application.port.out.auctions.AuctionWonEventPort;
import ch.unisg.tapas.auctionhouse.application.port.out.auctions.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.domain.*;
import ch.unisg.tapas.common.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service that implements the {@link LaunchAuctionUseCase} to start an auction. If a deadline is
 * specified for the auction, the service automatically closes the auction at the deadline. If a
 * deadline is not specified, the service closes the auction after 10s by default.
 */
@Component
public class StartAuctionService implements LaunchAuctionUseCase {
    private static final Logger LOGGER = LogManager.getLogger(StartAuctionService.class);

    private final static int DEFAULT_AUCTION_DEADLINE_MILLIS = 10000;

    // Event port used to publish an auction started event
    private final AuctionStartedEventPort auctionStartedEventPort;
    // Event port used to publish an auction won event
    private final AuctionWonEventPort auctionWonEventPort;

    private final ScheduledExecutorService service;
    private final AuctionRegistry auctions;

    @Autowired
    private ConfigProperties config;

    public StartAuctionService(AuctionStartedEventPort auctionStartedEventPort,
                               AuctionWonEventPort auctionWonEventPort){
        this.auctionStartedEventPort = auctionStartedEventPort;
        this.auctionWonEventPort = auctionWonEventPort;
        this.auctions = AuctionRegistry.getInstance();
        this.service = Executors.newScheduledThreadPool(1);
    }

    /**
     * Launches an auction.
     *
     * @param command the domain command used to launch the auction (see {@link LaunchAuctionCommand})
     * @return the launched auction
     */
    @Override
    public Auction.AuctionId launchAuction(LaunchAuctionCommand command) {
        Auction.AuctionDeadline deadline = (command.getDeadline() == null) ?
            new Auction.AuctionDeadline(DEFAULT_AUCTION_DEADLINE_MILLIS) : command.getDeadline();


        Auction.InputData inputData = command.getInputData();
        Auction.AuctionFeedId auctionFeedId = new Auction.AuctionFeedId("empty");

        // Create a new auction and add it to the auction registry
        Auction auction = new Auction(new Auction.AuctionHouseUri(config.getAuctionHouseUri()),
            command.getTaskUri(), command.getTaskType(), deadline, auctionFeedId, inputData);
        auction.setAuctionFeedId(new Auction.AuctionFeedId("ch/unisg/tapas/auctions/group5/" + auction.getAuctionId().getValue()));

        auctions.addAuction(auction);

        // Schedule the closing of the auction at the deadline
        service.schedule(new CloseAuctionTask(auction.getAuctionId()), deadline.getValue(),
            TimeUnit.MILLISECONDS);

        // Publish an auction started event
        AuctionStartedEvent auctionStartedEvent = new AuctionStartedEvent(auction);
        auctionStartedEventPort.publishAuctionStartedEvent(auctionStartedEvent);

        return auction.getAuctionId();
    }

    /**
     * This task closes the auction at the deadline and selects a winner if any bids were placed. It
     * also sends out associated events and commands.
     */
    private class CloseAuctionTask implements Runnable {
        Auction.AuctionId auctionId;

        public CloseAuctionTask(Auction.AuctionId auctionId) {
            this.auctionId = auctionId;
        }

        @Override
        public void run() {

            try {
                Auction auction = auctions.getAuctionById(auctionId);

                Optional<Bid> bid = auction.selectBid();

                // Close the auction
                auction.close();

                if (bid.isPresent()) {
                    // Notify the bidder
                    Bid.BidderName bidderName = bid.get().getBidderName();
                    LOGGER.info("Auction #" + auction.getAuctionId().getValue() + " for task "
                            + auction.getTaskUri().getValue() + " won by " + bidderName.getValue());

                    // Send an auction won event for the winning bid
                    auctionWonEventPort.publishAuctionWonEvent(new AuctionWonEvent(bid.get()));
                } else {
                    LOGGER.info("Auction #" + auction.getAuctionId().getValue() + " ended with no bids for task "
                            + auction.getTaskUri().getValue());
                }
            } catch (AuctionNotFoundError e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
