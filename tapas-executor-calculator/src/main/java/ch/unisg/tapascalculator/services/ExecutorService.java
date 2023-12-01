package ch.unisg.tapascalculator.services;

import ch.unisg.tapascalculator.domain.Task;
import ch.unisg.tapascalculator.CalculatorExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutorService {

    private ExecutionUseCase executor = new CalculatorExecutor();

    @Autowired
    private StatusService statusService;

    public Task executeTask(Task task) {
        // publish task started for shadow task
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
