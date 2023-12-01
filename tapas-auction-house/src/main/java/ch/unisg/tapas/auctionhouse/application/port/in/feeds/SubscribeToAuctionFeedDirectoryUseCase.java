package ch.unisg.tapas.auctionhouse.application.port.in.feeds;

import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;

public interface SubscribeToAuctionFeedDirectoryUseCase {

    void subscribeToDirectory(SubscribeToAuctionFeedDirectoryCommand command) throws OutputPortError;
}
