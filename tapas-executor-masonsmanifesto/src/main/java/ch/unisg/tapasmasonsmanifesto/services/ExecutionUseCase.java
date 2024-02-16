package ch.unisg.tapasmasonsmanifesto.services;
import ch.unisg.tapasmasonsmanifesto.domain.Task;

import java.util.concurrent.CompletableFuture;

public interface ExecutionUseCase {
    CompletableFuture<Task> executeTask(final Task task);
}