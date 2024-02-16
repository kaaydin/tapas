package ch.unisg.tapasmasonsmanifesto;

import ch.unisg.tapasmasonsmanifesto.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;


class MasonExecutorTest {
    private MasonExecutor masonExecutor;

    @BeforeEach
    void setUp() {
        masonExecutor = new MasonExecutor();
    }
    @Test
    void testExecuteTaskSuccess() throws ExecutionException, InterruptedException {
        Task task = new Task();

        CompletableFuture<Task> future = masonExecutor.executeTask(task);
        Task result = future.get();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getOutput());
    }

    @Test
    void testExecuteTaskSucceedsWithInput() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setInput("anything");
        CompletableFuture<Task> future = masonExecutor.executeTask(task);
        Task result = future.get();

        assertNotNull(result);
        assertNotNull(result.getOutput());
    }
}
