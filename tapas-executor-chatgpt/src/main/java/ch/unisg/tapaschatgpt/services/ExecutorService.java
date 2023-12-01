package ch.unisg.tapaschatgpt.services;

import ch.unisg.tapaschatgpt.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.unisg.tapaschatgpt.ChatGPTExecutor;


@Service
public class ExecutorService {

    private ExecutionUseCase executor = new ChatGPTExecutor();

    @Autowired
    private StatusService statusService;

    public Task executeTask(Task task) {
        // publish task started
        statusService.publishTaskStartedEvent(task.getTaskLocation());

        // publish task started for original task
        if (task.getOriginalTaskUri() != null) {
            statusService.publishTaskStartedEvent(task.getOriginalTaskUri());
        }

        // execute task
        Task completedTask = executor.executeTask(task);

        // publish task completed for shadow task
        statusService.publishTaskStatusEvent(task.getTaskLocation(), Task.Status.EXECUTED, task.getOutput());

        // publish task completed for original task
        if (task.getOriginalTaskUri() != null) {
            statusService.publishTaskStatusEvent(task.getOriginalTaskUri(), Task.Status.EXECUTED, task.getOutput());
        }

        return completedTask;
    }

}
