package ch.unisg.tapas.auctionhouse.adapter.out.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.TapasMqttClient;
import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.AuctionHouseEventMqttDispatcher;
import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.AuctionStartedEventListenerMqttAdapter;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.SubscribeToAuctionFeedPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mqtt")
@RequiredArgsConstructor
public class SubscribeToAuctionFeedMqttAdapter implements SubscribeToAuctionFeedPort {
    private static final Logger LOGGER = LogManager.getLogger(SubscribeToAuctionFeedMqttAdapter.class);

    private final AuctionHouseEventMqttDispatcher dispatcher;
    private final AuctionHouseEventMqttDispatcher dispatcherBids;

    private final AuctionStartedEventListenerMqttAdapter auctionStartedEventListenerMqttAdapter;

    @Autowired
    private final ConfigProperties config;

    @Override
    public void subscribeToFeed(Auction.AuctionFeedId feedId) {
        LOGGER.info("Subscribing to auction feed via MQTT: " + feedId.getValue());

        dispatcher.registerTopicAndListener(feedId.getValue(), auctionStartedEventListenerMqttAdapter);
        TapasMqttClient mqttClient = TapasMqttClient.getInstance(config.getMqttBrokerAddress(), dispatcher);

        try {
            mqttClient.subscribeToTopic(feedId.getValue());
        } catch (MqttException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
