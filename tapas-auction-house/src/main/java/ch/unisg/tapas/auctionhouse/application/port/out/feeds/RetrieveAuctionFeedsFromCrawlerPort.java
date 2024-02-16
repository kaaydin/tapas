package ch.unisg.tapas.auctionhouse.application.port.out.feeds;

import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;
import ch.unisg.tapas.auctionhouse.domain.Auction;

import java.net.URI;
import java.util.List;

public interface RetrieveAuctionFeedsFromCrawlerPort {
    List<Auction.AuctionFeedId> retrieveAuctionFeedsFromCrawler(URI peerStartUri, List<String> topicstoSubscribe) throws OutputPortError;

}
