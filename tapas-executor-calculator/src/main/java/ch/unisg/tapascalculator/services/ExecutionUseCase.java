package ch.unisg.tapascalculator.services;
import ch.unisg.tapascalculator.domain.Task;

public interface ExecutionUseCase {
    Task executeTask(final Task task);
}
