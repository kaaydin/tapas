package ch.unisg.tapascalculator.services;
import ch.unisg.tapascalculator.domain.Task;

import java.util.concurrent.CompletableFuture;

public interface ExecutionUseCase {
    CompletableFuture<Task> executeTask(final Task task);
}
