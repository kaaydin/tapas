package ch.unisg.tapaschatgpt;

import static org.junit.jupiter.api.Assertions.*;
import ch.unisg.tapaschatgpt.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChatGPTExecutorTest {
    private ChatGPTExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new ChatGPTExecutor();
    }

    @Test
    void testExecuteTaskWithNullInput() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput(null);
        CompletableFuture<Task> result = executor.executeTask(task);

        assertNull(result.get().getOutput(), "Output should be null for null input");
    }

    @Test
    void testExecuteTaskWithEmptyInput() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("");

        CompletableFuture<Task> result = executor.executeTask(task);

        assertNull(result.get().getOutput(), "Output should be null for empty input");
    }

    @Test
    void testExecuteTaskWithValidInput() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("anything");

        CompletableFuture<Task> result = executor.executeTask(task);

        assertNotNull(result.get().getOutput());
    }

}
