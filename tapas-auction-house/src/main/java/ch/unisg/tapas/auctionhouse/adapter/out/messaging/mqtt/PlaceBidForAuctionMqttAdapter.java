package ch.unisg.tapas.auctionhouse.adapter.out.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.TapasMqttClient;
import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.AuctionHouseEventMqttDispatcher;
import ch.unisg.tapas.auctionhouse.application.port.out.bids.PlaceBidForAuctionPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import ch.unisg.tapas.common.ConfigProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.awt.desktop.SystemEventListener;

/**
 * This class is a template for implementing an MQTT adapter that places bid.
 */
@Component
@Profile("mqtt")
@RequiredArgsConstructor
public class PlaceBidForAuctionMqttAdapter implements PlaceBidForAuctionPort {
    private static final Logger LOGGER = LogManager.getLogger(PlaceBidForAuctionMqttAdapter.class);

    private final ConfigProperties config;
    private final AuctionHouseEventMqttDispatcher auctionEventsMqttDispatcher;

    @Override
    public void placeBid(Auction auction, Bid bid) {
        LOGGER.info("Sending bid via MQTT");
        TapasMqttClient mqttClient = TapasMqttClient.getInstance(config.getMqttBrokerAddress(), auctionEventsMqttDispatcher);

        System.out.println(auction);
        System.out.println(bid);
        System.out.println(auction.getAuctionFeedId().getValue().toString());

        try {
            String message = BidJsonRepresentation.serialize(bid);
            mqttClient.publish(auction.getAuctionFeedId().getValue().toString(), message);
        } catch (JsonProcessingException | MqttException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
