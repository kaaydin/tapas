package ch.unisg.tapascherrybot.services;

import ch.unisg.tapascherrybot.CherrybotExecutor;
import ch.unisg.tapascherrybot.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ExecutorService {

    private ExecutionUseCase executor = new CherrybotExecutor();

    @Autowired
    private StatusService statusService;

    public Task executeTask(Task task) throws ExecutionException, InterruptedException {
        // publish task started
        statusService.publishTaskStartedEvent(task.getTaskLocation());

        // publish task started for original task
        if (task.getOriginalTaskUri() != null) {
            statusService.publishTaskStartedEvent(task.getOriginalTaskUri());
        }

        // execute task
        CompletableFuture<Task> completedTask = executor.executeTask(task);

        // publish task completed for shadow task
        statusService.publishTaskStatusEvent(completedTask.get().getTaskLocation(), Task.Status.EXECUTED, completedTask.get().getOutput());

        // publish task completed for original task
        if (task.getOriginalTaskUri() != null) {
            statusService.publishTaskStatusEvent(completedTask.get().getOriginalTaskUri(), Task.Status.EXECUTED, completedTask.get().getOutput());
        }

        return completedTask.get();
    }

}
