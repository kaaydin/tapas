package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedUseCase;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.SubscribeToAuctionFeedPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeToAuctionFeedService implements SubscribeToAuctionFeedUseCase {
    private final SubscribeToAuctionFeedPort subscribeToAuctionFeedPort;

    @Override
    public void subscribeToFeed(SubscribeToAuctionFeedCommand command) {
        subscribeToAuctionFeedPort.subscribeToFeed(command.getFeedId());
    }
}
