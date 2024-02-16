package ch.unisg.tapascalculator;

import ch.unisg.tapascalculator.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorExecutorTest {

    private CalculatorExecutor calculatorExecutor;

    @BeforeEach
    void setUp() {
        calculatorExecutor = new CalculatorExecutor();
    }

    @Test
    void testAddition() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("3+2");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("5", executedTask.get().getOutput());
    }

    @Test
    void testSubtraction() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("5-2");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("3", executedTask.get().getOutput());
    }

    @Test
    void testMultiplication() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("4*2");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("8", executedTask.get().getOutput());
    }

    @Test
    void testDivision() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("8/2");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("4", executedTask.get().getOutput());
    }

    @Test
    void testDivisionByZero() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("8/0");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("Error: Division by zero", executedTask.get().getOutput());
    }

    @Test
    void testIllegalOperation() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("2^2");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("An error occurred: Unsupported operation: ^", executedTask.get().getOutput());
    }

    @Test
    void testInvalidNumberFormat() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("a+2");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("Invalid number format", executedTask.get().getOutput());
    }

    @Test
    void testInvalidInputFormat() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("2+");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        assertEquals("An error occurred: Invalid input format", executedTask.get().getOutput());
    }

    @Test
    void testNullInput() {
        Task task = new Task();
        task.setInput(null);
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        ExecutionException executionException = assertThrows(
                ExecutionException.class,
                () -> executedTask.get() // This waits for the CompletableFuture to complete
        );

        Throwable cause = executionException.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertTrue(cause.getMessage().contains("Task or task input cannot be null or empty"));
    }

    @Test
    void testEmptyInput() {
        Task task = new Task();
        task.setInput("");
        CompletableFuture<Task> executedTask = calculatorExecutor.executeTask(task);
        ExecutionException executionException = assertThrows(
                ExecutionException.class,
                () -> executedTask.get() // This waits for the CompletableFuture to complete
        );

        Throwable cause = executionException.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertTrue(cause.getMessage().contains("Task or task input cannot be null or empty"));
    }
}
