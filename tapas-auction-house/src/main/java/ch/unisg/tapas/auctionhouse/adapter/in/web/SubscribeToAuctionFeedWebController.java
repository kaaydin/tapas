package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles HTTP requests for subscribing to an auction feed. This controller implements
 * the {@link SubscribeToAuctionFeedUseCase} use case using the
 * {@link SubscribeToAuctionFeedCommand} command.
 */
@RestController
@RequiredArgsConstructor
public class SubscribeToAuctionFeedWebController {
    private static final Logger LOGGER = LogManager.getLogger(SubscribeToAuctionFeedWebController.class);

    private final SubscribeToAuctionFeedUseCase subscribeToAuctionFeedUseCase;

    /**
     * Handles an HTTP POST request for subscribing to an auction feed.
     *
     * @param payload the identifier of the auction feed to be subscribed to
     * @return a 200 OK status code if the subscription is successful
     */
    @PostMapping(path = "/subscribeToFeed/")
    public ResponseEntity<Void> subscribeToFeed(@RequestBody String payload) {
        LOGGER.info("Received request to subscribe to auction feed: " + payload);

        SubscribeToAuctionFeedCommand command = new SubscribeToAuctionFeedCommand(new Auction.AuctionFeedId(payload));
        subscribeToAuctionFeedUseCase.subscribeToFeed(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
