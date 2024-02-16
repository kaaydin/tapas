package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.feeds.CrawlAuctionFeedsCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.feeds.CrawlAuctionFeedsUseCase;
import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.RetrieveAuctionFeedsFromCrawlerPort;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.SubscribeToAuctionFeedPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("http-websub")
@RequiredArgsConstructor
public class CrawlAuctionFeedsService implements CrawlAuctionFeedsUseCase {
    private final RetrieveAuctionFeedsFromCrawlerPort retrieveAuctionFeedsFromCrawlerPort;
    private final SubscribeToAuctionFeedPort subscribeToAuctionFeedPort;

    @Override
    public void crawlAuctions(CrawlAuctionFeedsCommand command) throws OutputPortError {
        List<Auction.AuctionFeedId> auctionHouseEndpoints =
            retrieveAuctionFeedsFromCrawlerPort.retrieveAuctionFeedsFromCrawler(command.getEntryPoint(), command.getTopicsToSubscribe());

        for (Auction.AuctionFeedId endpoint : auctionHouseEndpoints) {
            subscribeToAuctionFeedPort.subscribeToFeed(endpoint);
        }
    }
}
