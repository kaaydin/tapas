package ch.unisg.tapaschatgpt.services;
import ch.unisg.tapaschatgpt.domain.Task;

public interface ExecutionUseCase {
    Task executeTask(final Task task);
}