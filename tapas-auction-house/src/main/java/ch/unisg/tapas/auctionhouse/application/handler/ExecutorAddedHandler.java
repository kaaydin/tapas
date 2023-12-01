package ch.unisg.tapas.auctionhouse.application.handler;

import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorAddedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorAddedEventHandler;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import org.springframework.stereotype.Component;

@Component
public class ExecutorAddedHandler implements ExecutorAddedEventHandler {

    @Override
    public boolean handleNewExecutorEvent(ExecutorAddedEvent executorAddedEvent) {
        return ExecutorRegistry.getInstance().addExecutor(executorAddedEvent.getTaskType(),
            executorAddedEvent.getExecutorId());
    }
}
