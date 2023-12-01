 package ch.unisg.tapas.auctionhouse.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.net.URI;
import java.util.*;

/**
 * Domain entity that models an auction.
 */
public class Auction {
    // Auctions have two possible states:
    // - open: waiting for bids
    // - closed: the auction deadline has expired, there may or may not be a winning bid
    public enum Status {
        OPEN, CLOSED
    }

    // One way to generate auction identifiers is incremental starting from 1. This makes identifiers
    // predictable, which can help with debugging when multiple parties are interacting, but it also
    // means that auction identifiers are not universally unique unless they are part of a URI.
    // An al  ternative would be to use UUIDs (see constructor).
    private static long AUCTION_COUNTER = 1;

    @Getter
    private AuctionId auctionId;

    @Getter
    private AuctionStatus auctionStatus;

    // URI that identifies the auction house that started this auction. Given a uniform, standard
    // HTTP API for auction houses, this URI can then be used as a base URI for interacting with
    // the identified auction house.
    @Getter
    private final AuctionHouseUri auctionHouseUri;

    // Identifier of the auction feed where this auction was started (can be null)
    @Getter @Setter
    private AuctionFeedId auctionFeedId;

    @Getter @Setter
    private InputData inputData;

    // URI that identifies the task for which the auction was launched. URIs are uniform identifiers
    // and can be referenced independent of context: because we have defined a uniform HTTP API for
    // TAPAS-Tasks, we can dereference this URI to retrieve a complete representation of the
    // auctioned task.
    @Getter
    private final AuctionedTaskUri taskUri;

    // The type of the task being auctioned. We could also retrieve the task type by dereferencing
    // the task's URI, but given that the bidding is defined primarily based on task types, knowing
    // the task type avoids an additional HTTP request.
    @Getter
    private final AuctionedTaskType taskType;

    // The deadline by which bids can be placed. Once the deadline expires, the auction is closed.
    @Getter
    private final AuctionDeadline deadline;

    // Available bids.
    @Getter
    private final List<Bid> bids;

    /**
     * Constructs an auction.
     *
     * @param auctionHouseUri the URI of the auction hause that started the auction
     * @param taskUri the URI of the task being auctioned
     * @param taskType the type of the task being auctioned
     * @param deadline the deadline by which the auction is open for bids
     */
    public Auction(AuctionHouseUri auctionHouseUri, AuctionedTaskUri taskUri,
                   AuctionedTaskType taskType, AuctionDeadline deadline, AuctionFeedId auctionFeedId, InputData inputData) {
        // Generates an incremental identifier
        this.auctionId = new AuctionId("" + AUCTION_COUNTER ++);
        // As an alternative, we could also generate an UUID
        // this.auctionId = new AuctionId(UUID.randomUUID().toString());

        this.auctionStatus = new AuctionStatus(Status.OPEN);

        this.auctionHouseUri = auctionHouseUri;
        this.auctionFeedId = auctionFeedId;
        this.taskUri = taskUri;
        this.taskType = taskType;
        this.inputData = inputData;
        this.deadline = deadline;
        this.bids = new ArrayList<>();
    }

    public Auction(AuctionId auctionId, AuctionHouseUri auctionHouseUri, AuctionedTaskUri taskUri,
                   AuctionedTaskType taskType, AuctionDeadline deadline, AuctionStatus status, AuctionFeedId auctionFeedId, InputData inputData) {
        this.auctionId = auctionId;
        this.auctionHouseUri = auctionHouseUri;
        this.auctionFeedId = auctionFeedId;
        this.taskUri = taskUri;
        this.taskType = taskType;
        this.deadline = deadline;
        this.auctionStatus = status;
        this.bids = new ArrayList<>();
        this.inputData = inputData;
    }

    /**
     * Constructs an auction.
     *
     * @param auctionId the identifier of the auction
     * @param auctionHouseUri the URI of the auction hause that started the auction
     * @param taskUri the URI of the task being auctioned
     * @param taskType the type of the task being auctioned
     * @param deadline the deadline by which the auction is open for bids
     */
    public Auction(AuctionId auctionId, AuctionHouseUri auctionHouseUri, AuctionedTaskUri taskUri,
            AuctionedTaskType taskType, AuctionDeadline deadline, AuctionFeedId auctionFeedId, InputData inputData) {
        this(auctionHouseUri, taskUri, taskType, deadline, auctionFeedId, inputData);
        this.auctionId = auctionId;
    }

    /**
     * Places a bid for this auction.
     *
     * @param bid the bid
     */
    public void addBid(Bid bid) {
        bids.add(bid);
    }

    /**
     * Selects a bid randomly from the bids available for this auction.
     *
     * @return a winning bid or Optional.empty if no bid was made in this auction.
     */
    public Optional<Bid> selectBid() {
        if (bids.isEmpty()) {
            return Optional.empty();
        }

        int index = new Random().nextInt(bids.size());
        return Optional.of(bids.get(index));
    }

    /**
     * Checks if the auction is open for bids.
     *
     * @return true if open for bids, false if the auction is closed
     */
    public boolean isOpen() {
        return auctionStatus.getValue() == Status.OPEN;
    }

    /**
     * Closes the auction. Called by the StartAuctionService after the auction deadline has expired.
     */
    public void close() {
        auctionStatus = new AuctionStatus(Status.CLOSED);
    }

    /*
     * Definitions of Value Objects
     */

    @Value
    public static class AuctionId {
        String value;
    }

    @Value
    public static class InputData {
        String value;
    }
    @Value
    public static class AuctionStatus {
        Status value;
    }

    @Value
    public static class AuctionHouseUri {
        URI value;
    }

    @Value
    public static class AuctionFeedId {
        String value;
    }

    @Value
    public static class AuctionedTaskUri {
        URI value;
    }

    @Value
    public static class AuctionedTaskType {
        String value;
    }

    @Value
    public static class AuctionDeadline {
        int value;
    }
}
