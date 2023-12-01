package ch.unisg.tapas.auctionhouse.application.port.in.executors;

public interface ExecutorAddedEventHandler {

    boolean handleNewExecutorEvent(ExecutorAddedEvent executorAddedEvent);
}
