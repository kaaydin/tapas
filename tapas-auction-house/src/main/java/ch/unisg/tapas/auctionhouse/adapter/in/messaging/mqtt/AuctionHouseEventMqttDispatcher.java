package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;
import ch.unisg.tapas.common.ConfigProperties;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import ch.unisg.tapas.auctionhouse.domain.Auction;


import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Dispatches MQTT messages for known topics to associated event listeners. Used in conjunction with
 * {@link ch.unisg.tapas.auctionhouse.adapter.common.clients.TapasMqttClient}.
 * <p>
 * This is where you can define MQTT topics and map them to event listeners, see
 * {@link AuctionHouseEventMqttDispatcher#initRouter()} and
 * {@link AuctionHouseEventMqttDispatcher#registerTopicAndListener(String, AuctionHouseEventMqttListener)}).
 * <p>
 * This class is only provided as an example to help you bootstrap the project. You are welcomed to
 * change or extend this class as you see fit.
 */

@Component
@Profile("mqtt")
public class AuctionHouseEventMqttDispatcher {
    private final ConfigProperties config;
    private final Map<String, AuctionHouseEventMqttListener> router;
    private final ExecutorAddedEventListenerMqttAdapter executorAddedEventListenerMqttAdapter;
    //private final BidsEventListenerMqttAdapter bidsEventListenerMqttAdapter;

    public AuctionHouseEventMqttDispatcher(ConfigProperties config, ExecutorAddedEventListenerMqttAdapter executorAddedEventListenerMqttAdapter) {
        //BidsEventListenerMqttAdapter bidsEventListenerMqttAdapter
        this.config = config;
        this.executorAddedEventListenerMqttAdapter = executorAddedEventListenerMqttAdapter;
        //this.bidsEventListenerMqttAdapter = bidsEventListenerMqttAdapter;
        this.router = new Hashtable<>();


        initRouter();
    }

    public void registerTopicAndListener(String topic, AuctionHouseEventMqttListener listener) {
        router.put(topic, listener);
        //router.put(topic, bidsEventListenerMqttAdapter);
        //router.put(topic, bidsEventListenerMqttAdapter);
        System.out.println("Topic: " + topic);

    }

    private void initRouter() {
        router.put(config.getMqttExecutorsTopic(), executorAddedEventListenerMqttAdapter);
        System.out.println("Executor topic: " + config.getMqttExecutorsTopic());
    }
    /**
     * Returns all topics registered with this dispatcher.
     *
     * @return the set of registered topics
     */
    public Set<String> getAllTopics() {
        return router.keySet();
    }

    /**
     * Dispatches an event received via MQTT for a given topic.
     *
     * @param topic   the topic for which the MQTT message was received
     * @param message the received MQTT message
     */
    public void dispatchEvent(String topic, MqttMessage message) {
        AuctionHouseEventMqttListener listener = router.get(topic);
        listener.handleEvent(message);
    }
}
