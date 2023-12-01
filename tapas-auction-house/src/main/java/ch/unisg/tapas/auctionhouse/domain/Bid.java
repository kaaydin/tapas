package ch.unisg.tapas.auctionhouse.domain;

import lombok.Getter;
import lombok.Value;

import java.net.URI;

/**
 * Domain entity that models a bid.
 */
public class Bid {
    // The identifier of the auction for which the bid is placed
    @Getter
    private final Auction.AuctionId auctionId;

    // The name of the bidder, i.e. the identifier of the TAPAS group
    @Getter
    private final BidderName bidderName;

    // URI that identifies the auction house of the bidder. Given a uniform, standard HTTP API for
    // auction houses, this URI can then be used as a base URI for interacting with the auction house
    // of the bidder.
    @Getter
    private final AuctionHouseUri auctionHouseUri;

    // URI that identifies the TAPAS-Tasks task list of the bidder. Given a uniform, standard HTTP API
    // for TAPAS-Tasks, this URI can then be used as a base URI for interacting with the list of tasks
    // of the bidder, e.g. to delegate a task.
    @Getter
    private final TaskListUri taskListUri;

    /**
     * Constructs a bid.
     *
     * @param auctionId the identifier of the auction for which the bid is placed
     * @param bidderName the name of the bidder, i.e. the identifier of the TAPAS group
     * @param auctionHouseUri the URI of the bidder's auction house
     * @param taskListUri the URI fo the bidder's list of tasks
     */
    public Bid(Auction.AuctionId auctionId, BidderName bidderName, AuctionHouseUri auctionHouseUri,
               TaskListUri taskListUri) {
        this.auctionId = auctionId;
        this.bidderName = bidderName;
        this.auctionHouseUri = auctionHouseUri;
        this.taskListUri = taskListUri;
    }

    /*
     * Definitions of Value Objects
     */

    @Value
    public static class BidderName {
        private String value;
    }

    @Value
    public static class AuctionHouseUri {
        private URI value;
    }

    @Value
    public static class TaskListUri {
        private URI value;
    }
}
