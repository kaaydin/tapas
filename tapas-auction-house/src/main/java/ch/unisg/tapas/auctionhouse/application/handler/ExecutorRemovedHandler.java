package ch.unisg.tapas.auctionhouse.application.handler;

import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorRemovedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.executors.ExecutorRemovedEventHandler;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import org.springframework.stereotype.Component;

/**
 * Handler for executor removed events. It removes the executor from this auction house's executor
 * registry.
 */
@Component
public class ExecutorRemovedHandler implements ExecutorRemovedEventHandler {

    @Override
    public boolean handleExecutorRemovedEvent(ExecutorRemovedEvent executorRemovedEvent) {
        return ExecutorRegistry.getInstance().removeExecutor(executorRemovedEvent.getExecutorId());
    }
}
