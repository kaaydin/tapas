package ch.unisg.tapascherrybot.services;
import ch.unisg.tapascherrybot.domain.Task;

import java.util.concurrent.CompletableFuture;

public interface ExecutionUseCase {
    CompletableFuture<Task> executeTask(final Task task);
}
