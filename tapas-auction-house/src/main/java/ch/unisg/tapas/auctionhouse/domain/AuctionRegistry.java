package ch.unisg.tapas.auctionhouse.domain;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Registry that keeps an in-memory history of auctions (both open for bids and closed). This class
 * is a singleton. See also {@link Auction}.
 */
public class AuctionRegistry {
    private static AuctionRegistry registry;

    private final Map<Auction.AuctionId, Auction> auctions;

    private AuctionRegistry() {
        this.auctions = new Hashtable<>();
    }

    /**
     * Retrieves a reference to the auction registry.
     *
     * @return the auction registry
     */
    public static synchronized AuctionRegistry getInstance() {
        if (registry == null) {
            registry = new AuctionRegistry();
        }

        return registry;
    }

    /**
     * Adds a new auction to the registry
     * @param auction the new auction
     */
    public void addAuction(Auction auction) {
        auctions.put(auction.getAuctionId(), auction);
    }

    /**
     * Places a bid. See also {@link Bid}.
     *
     * @param bid the bid to be placed.
     * @return false if the bid is for an auction with an unknown identifier, true otherwise
     */
    public boolean placeBid(Bid bid) throws AuctionNotFoundError {
        if (!containsAuctionWithId(bid.getAuctionId())) {
            return false;
        }

        Auction auction = getAuctionById(bid.getAuctionId());
        auction.addBid(bid);
        auctions.put(bid.getAuctionId(), auction);

        return true;
    }

    /**
     * Checks if the registry contains an auction with the given identifier.
     *
     * @param auctionId the auction's identifier
     * @return true if the registry contains an auction with the given identifier, false otherwise
     */
    public boolean containsAuctionWithId(Auction.AuctionId auctionId) {
        return auctions.containsKey(auctionId);
    }

    /**
     * Retrieves the auction with the given identifier if it exists.
     *
     * @param auctionId the auction's identifier
     * @return the auction or Optional.empty if the identifier is unknown
     */
    public Auction getAuctionById(Auction.AuctionId auctionId) throws AuctionNotFoundError {
        if (containsAuctionWithId(auctionId)) {
            return auctions.get(auctionId);
        }

        throw new AuctionNotFoundError();
    }

    /**
     * Retrieves all auctions in the registry.
     *
     * @return a collection with all auctions
     */
    public Collection<Auction> getAllAuctions() {
        return auctions.values();
    }

    /**
     * Retrieves only the auctions that are open for bids.
     *
     * @return a collection with all open auctions
     */
    public Collection<Auction> getOpenAuctions() {
        return getAllAuctions()
            .stream()
            .filter(auction -> auction.isOpen())
            .collect(Collectors.toList());
    }
}
