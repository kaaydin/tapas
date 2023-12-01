package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.bids.ReceiveBidUseCase;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * MQTT Listener Adapter that handles bid events.
 */
@Component
@Profile("mqtt")
@RequiredArgsConstructor
public class BidsEventListenerMqttAdapter implements AuctionHouseEventMqttListener {
    private static final Logger LOGGER = LogManager.getLogger(BidsEventListenerMqttAdapter.class);
    private final ReceiveBidUseCase receiveBidUseCase;

    @Override
    public boolean handleEvent(MqttMessage message) {
        String payload = new String(message.getPayload());
        LOGGER.info("Received bid event via MQTT: " + payload);

        try {
            Bid bid = BidJsonRepresentation.deserialize(payload);
            receiveBidUseCase.receiveBid(bid);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}
