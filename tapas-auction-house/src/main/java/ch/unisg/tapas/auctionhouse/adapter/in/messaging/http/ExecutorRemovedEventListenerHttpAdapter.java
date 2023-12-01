package ch.unisg.tapas.auctionhouse.adapter.in.messaging.http;

import ch.unisg.tapas.auctionhouse.application.handler.ExecutorRemovedHandler;
import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorRemovedEvent;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Template for receiving an executor removed event via HTTP.
 * Note: This class is only meant as a starting point. You should modify this class as you see fit!
 */
@RestController
public class ExecutorRemovedEventListenerHttpAdapter {

    @DeleteMapping(path = "/executors/{executorId}")
    public ResponseEntity<Void> handleExecutorRemovedEvent(@PathVariable("executorId") String executorId) {
        ExecutorRemovedEvent executorRemovedEvent = new ExecutorRemovedEvent(
            new ExecutorRegistry.ExecutorIdentifier(executorId)
        );

        ExecutorRemovedHandler executorRemovedHandler = new ExecutorRemovedHandler();
        executorRemovedHandler.handleExecutorRemovedEvent(executorRemovedEvent);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
