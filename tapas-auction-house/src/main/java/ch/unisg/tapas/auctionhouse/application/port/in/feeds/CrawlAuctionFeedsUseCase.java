package ch.unisg.tapas.auctionhouse.application.port.in.feeds;

import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;

public interface CrawlAuctionFeedsUseCase {

    void crawlAuctions(CrawlAuctionFeedsCommand command) throws OutputPortError;
}
