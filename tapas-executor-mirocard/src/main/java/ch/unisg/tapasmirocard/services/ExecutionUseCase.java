package ch.unisg.tapasmirocard.services;

import ch.unisg.tapasmirocard.domain.Task;

import java.util.concurrent.CompletableFuture;

public interface ExecutionUseCase {
    CompletableFuture<Task> executeTask(final Task task);
}
