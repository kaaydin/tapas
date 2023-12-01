package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.AuctionHouseEventMqttDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * MQTT client for your TAPAS application. This class is defined as a singleton, but it does not have
 * to be this way. This class is only provided as an example to help you bootstrap your project.
 * You are welcomed to change this class as you see fit.
 */
public class TapasMqttClient {
    private static final Logger LOGGER = LogManager.getLogger(TapasMqttClient.class);

    private static TapasMqttClient tapasClient = null;

    private MqttClient mqttClient;
    private final String mqttClientId;
    private final URI brokerAddress;

    private final MessageReceivedCallback messageReceivedCallback;

    private final AuctionHouseEventMqttDispatcher dispatcher;

    private TapasMqttClient(URI brokerAddress, AuctionHouseEventMqttDispatcher dispatcher) {
        this.mqttClientId = UUID.randomUUID().toString();
        this.brokerAddress = brokerAddress;

        // The MQTT client is initialized when either connect() or publishMessage() are invoked
        this.mqttClient = null;
        this.messageReceivedCallback = new MessageReceivedCallback();

        this.dispatcher = dispatcher;
    }

    /**
     * Retrieves an instance of the Tapas MQTT client or creates one if it does not exist.
     *
     * @param brokerAddress the address of the MQTT broker
     * @param dispatcher a router that maps MQTT topics to event listeners
     * @return the Tapas MQTT client
     */
    public static synchronized TapasMqttClient getInstance(URI brokerAddress,
                                                           AuctionHouseEventMqttDispatcher dispatcher) {

        if (tapasClient == null) {
            tapasClient = new TapasMqttClient(brokerAddress, dispatcher);
        }

        return tapasClient;
    }

    /**
     * Connects to the MQTT broker. This method need to be called before publishing messages or
     * subscribing to topics.
     *
     * @throws MqttException
     */
    public void connect() throws MqttException {
        mqttClient = new MqttClient(brokerAddress.toASCIIString(), mqttClientId, new MemoryPersistence());
        mqttClient.connect();
        mqttClient.setCallback(messageReceivedCallback);

        subscribeToAllRegisteredTopics();
    }

    /**
     * Disconnects from the MQTT broker
     *
     * @throws MqttException
     */
    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    /**
     * Publishes a message to an MQTT topic
     *
     * @param topic the MQTT topic encoded as a string
     * @param message the message to be published
     * @throws MqttException
     */
    public void publish(String topic, String message) throws MqttException {
        if (mqttClient == null) {
            connect();
        }

        MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
        mqttClient.publish(topic, mqttMessage);
    }

    /**
     * Subscribes to an MQTT topic to receive messages published on that topic
     *
     * @param topic the MQTT topic encoded as a string
     * @throws MqttException
     */
    public void subscribeToTopic(String topic) throws MqttException {
        if (mqttClient == null) {
            connect();
        }

        mqttClient.subscribe(topic);
    }

    private void subscribeToAllRegisteredTopics() throws MqttException {
        for (String topic : dispatcher.getAllTopics()) {
            subscribeToTopic(topic);
        }
    }

    private class MessageReceivedCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {  }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            LOGGER.info("Received new MQTT message for topic " + topic + ": "
                + new String(message.getPayload()));

            if (topic != null && !topic.isEmpty()) {
                dispatcher.dispatchEvent(topic, message);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {  }
    }
}
