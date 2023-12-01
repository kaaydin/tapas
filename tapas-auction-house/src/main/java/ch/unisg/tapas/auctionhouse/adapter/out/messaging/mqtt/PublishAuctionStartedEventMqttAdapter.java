package ch.unisg.tapas.auctionhouse.adapter.out.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.TapasMqttClient;
import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.AuctionHouseEventMqttDispatcher;
import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.BidsEventListenerMqttAdapter;
import ch.unisg.tapas.auctionhouse.application.port.out.auctions.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;
import ch.unisg.tapas.common.ConfigProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This class is a template for publishing auction started events via MQTT.
 */
@Component
@Profile("mqtt")
@RequiredArgsConstructor
public class PublishAuctionStartedEventMqttAdapter implements AuctionStartedEventPort {
    private static final Logger LOGGER = LogManager.getLogger(PublishAuctionStartedEventMqttAdapter.class);

    private final ConfigProperties config;
    private final AuctionHouseEventMqttDispatcher auctionEventsMqttDispatcher;
    private final BidsEventListenerMqttAdapter bidsEventListenerMqttAdapter;

    @Override
    public void publishAuctionStartedEvent(AuctionStartedEvent event) {
        LOGGER.info("Publishing auction started event via MQTT");

        TapasMqttClient mqttClient = TapasMqttClient.getInstance(config.getMqttBrokerAddress(),
            auctionEventsMqttDispatcher);

        try {
            String message = AuctionJsonRepresentation.serialize(event.getAuction());
            mqttClient.publish(config.getMqttAuctionsTopic(), message);
            LOGGER.info("Subscribing to own auction feed via MQTT: " + event.getAuction().getAuctionFeedId().getValue());
            auctionEventsMqttDispatcher.registerTopicAndListener(event.getAuction().getAuctionFeedId().getValue(), bidsEventListenerMqttAdapter);
            mqttClient.subscribeToTopic(event.getAuction().getAuctionFeedId().getValue());
        } catch (JsonProcessingException | MqttException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
