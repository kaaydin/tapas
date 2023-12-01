package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedDirectoryCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedDirectoryUseCase;
import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.RetrieveAuctionFeedsFromDirectoryPort;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.SubscribeToAuctionFeedPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("http-websub")
@RequiredArgsConstructor
public class SubscribeToAuctionFeedDirectoryService implements SubscribeToAuctionFeedDirectoryUseCase {
    private final RetrieveAuctionFeedsFromDirectoryPort retrieveAuctionFeedsFromDirectoryPort;
    private final SubscribeToAuctionFeedPort subscribeToAuctionFeedPort;

    @Override
    public void subscribeToDirectory(SubscribeToAuctionFeedDirectoryCommand command) throws OutputPortError {
        List<Auction.AuctionFeedId> auctionHouseEndpoints =
            retrieveAuctionFeedsFromDirectoryPort.retrieveFeedsFromDirectory(command.getFeedId());

        for (Auction.AuctionFeedId endpoint : auctionHouseEndpoints) {
            subscribeToAuctionFeedPort.subscribeToFeed(endpoint);
        }
    }
}
