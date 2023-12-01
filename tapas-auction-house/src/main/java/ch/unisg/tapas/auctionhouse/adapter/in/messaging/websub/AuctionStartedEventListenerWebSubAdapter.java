package ch.unisg.tapas.auctionhouse.adapter.in.messaging.websub;

import ch.unisg.tapas.auctionhouse.application.handler.AuctionStartedHandler;
import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.AuctionStartedEvent;
import ch.unisg.tapas.auctionhouse.domain.Auction;

import org.json.JSONArray;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

/**
 * This class is a template for handling auction started events received via WebSub
 */
@RestController
@Profile("http-websub")
@RequiredArgsConstructor
public class AuctionStartedEventListenerWebSubAdapter {
    private static final Logger LOGGER = LogManager.getLogger(AuctionStartedEventListenerWebSubAdapter.class);

    private final AuctionStartedHandler auctionStartedHandler;

    @PostMapping(path = "/auctions/notifications/")
    public void handleAuctionStartedEvent(@RequestBody String payload) {
        LOGGER.info("Received auction started event: " + payload);
        // parse JSON array and loop over all elements
        JSONArray jsonArray = new JSONArray(payload);
        jsonArray.forEach(item -> {
            // create auction from json representation
            try {
                Auction auction = AuctionJsonRepresentation.deserialize(item.toString());
                auctionStartedHandler.handleAuctionStartedEvent(new AuctionStartedEvent(auction));
            } catch (Exception e) {
                LOGGER.error("Error deserializing auction: " + e.getMessage());
            }
        });
    }

}
