package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.AuctionStartedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.AuctionStartedEventHandler;
import ch.unisg.tapas.auctionhouse.application.port.in.bids.ReceiveBidUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * MQTT Listener Adapter that handles auction started.
 */
@Component
@Profile("mqtt")
@RequiredArgsConstructor
public class AuctionStartedEventListenerMqttAdapter implements AuctionHouseEventMqttListener {
    private static final Logger LOGGER = LogManager.getLogger(AuctionStartedEventListenerMqttAdapter.class);
    private final AuctionStartedEventHandler auctionStartedEventHandler;
    private final ReceiveBidUseCase receiveBidUseCase;
    @Override
    public boolean handleEvent(MqttMessage message) {
        String payload = new String(message.getPayload());
        LOGGER.info("Received auction started event via MQTT: " + payload);

        try {
            Auction auction = AuctionJsonRepresentation.deserialize(payload);
            AuctionStartedEvent event = new AuctionStartedEvent(auction);
            auctionStartedEventHandler.handleAuctionStartedEvent(event);
            return true;
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            Bid bid = BidJsonRepresentation.deserialize(payload);
            receiveBidUseCase.receiveBid(bid);
            return true;
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

    }
}
