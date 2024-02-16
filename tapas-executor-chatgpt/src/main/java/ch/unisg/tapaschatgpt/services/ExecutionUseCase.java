package ch.unisg.tapaschatgpt.services;
import ch.unisg.tapaschatgpt.domain.Task;

import java.util.concurrent.CompletableFuture;

public interface ExecutionUseCase {
    CompletableFuture<Task> executeTask(final Task task);
}