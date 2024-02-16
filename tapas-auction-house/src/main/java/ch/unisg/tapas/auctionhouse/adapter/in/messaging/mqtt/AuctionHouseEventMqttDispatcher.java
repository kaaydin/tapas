package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.common.ConfigProperties;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

    public AuctionHouseEventMqttDispatcher(ConfigProperties config, ExecutorAddedEventListenerMqttAdapter executorAddedEventListenerMqttAdapter) {
        this.config = config;
        this.executorAddedEventListenerMqttAdapter = executorAddedEventListenerMqttAdapter;
        this.router = new Hashtable<>();


        initRouter();
    }

    public void registerTopicAndListener(String topic, AuctionHouseEventMqttListener listener) {
        router.put(topic, listener);
    }

    private void initRouter() {
        router.put(config.getMqttExecutorsTopic(), executorAddedEventListenerMqttAdapter);
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
