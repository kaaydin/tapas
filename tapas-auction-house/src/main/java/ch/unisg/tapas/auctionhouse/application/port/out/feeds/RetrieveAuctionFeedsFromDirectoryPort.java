package ch.unisg.tapas.auctionhouse.application.port.out.feeds;

import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;
import ch.unisg.tapas.auctionhouse.domain.Auction;

import java.net.URI;
import java.util.List;

/**
 * Port for retrieving auction feeds from an Auction House Resource Directory
 */
public interface RetrieveAuctionFeedsFromDirectoryPort {

    List<Auction.AuctionFeedId> retrieveFeedsFromDirectory(URI directoryUri) throws OutputPortError;
}
