package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorAddedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorAddedEventHandler;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Listener that handles the event of adding an executor to this TAPAS application. This class is only
 * provided as an example to help you bootstrap the project.
 */
@Component
@Profile("mqtt")
@RequiredArgsConstructor
public class ExecutorAddedEventListenerMqttAdapter implements AuctionHouseEventMqttListener {
    private static final Logger LOGGER = LogManager.getLogger(ExecutorAddedEventListenerMqttAdapter.class);
    private final ExecutorAddedEventHandler executorAddedEventHandler;

    @Override
    public boolean handleEvent(MqttMessage message) {
        String payload = new String(message.getPayload());

        try {
            // Note: this message representation is provided only as an example. You should use a
            // representation that makes sense in the context of your application.
            JsonNode data = new ObjectMapper().readTree(payload);

            String taskType = data.get("taskType").asText();
            String executorId = data.get("executorId").asText();

            ExecutorAddedEvent executorAddedEvent = new ExecutorAddedEvent(
                new ExecutorRegistry.ExecutorIdentifier(executorId),
                new Auction.AuctionedTaskType(taskType)
            );

            executorAddedEventHandler.handleNewExecutorEvent(executorAddedEvent);
        } catch (JsonProcessingException | NullPointerException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}
