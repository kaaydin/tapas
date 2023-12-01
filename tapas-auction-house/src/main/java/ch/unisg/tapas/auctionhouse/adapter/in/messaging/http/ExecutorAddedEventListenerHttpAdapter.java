package ch.unisg.tapas.auctionhouse.adapter.in.messaging.http;

import ch.unisg.tapas.auctionhouse.application.handler.ExecutorAddedHandler;
import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorAddedEvent;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Template for receiving an executor added event via HTTP.
 * Note: This class is only meant as a starting point. You should modify this class as you see fit!
 */
@RestController
public class ExecutorAddedEventListenerHttpAdapter {

    @PostMapping(path = "/executors/{taskType}/{executorId}")
    public ResponseEntity<Void> handleExecutorAddedEvent(@PathVariable("taskType") String taskType,
            @PathVariable("executorId") String executorId) {

        ExecutorAddedEvent executorAddedEvent = new ExecutorAddedEvent(
            new ExecutorRegistry.ExecutorIdentifier(executorId),
            new Auction.AuctionedTaskType(taskType)
        );

        ExecutorAddedHandler newExecutorHandler = new ExecutorAddedHandler();
        newExecutorHandler.handleNewExecutorEvent(executorAddedEvent);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
