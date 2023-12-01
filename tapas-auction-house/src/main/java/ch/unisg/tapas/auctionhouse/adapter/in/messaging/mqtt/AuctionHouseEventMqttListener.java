package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Interface for MQTT listeners
 */
interface AuctionHouseEventMqttListener {

    boolean handleEvent(MqttMessage message);
}
