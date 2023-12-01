package ch.unisg.tapas.auctionhouse.application.port.in.executors;

public interface ExecutorRemovedEventHandler {

    boolean handleExecutorRemovedEvent(ExecutorRemovedEvent executorRemovedEvent);
}
