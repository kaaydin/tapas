package ch.unisg.tapastasks.tasks.domain;

import lombok.Getter;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**This is our aggregate root**/
public class TaskList {
    private static final Logger LOGGER = LogManager.getLogger(TaskList.class);

    @Getter
    private final TaskListName taskListName;

    @Getter
    private final ListOfTasks listOfTasks;

    //Note: We do not care about the management of task lists, there is only one within this service
    //--> using the Singleton pattern here to make lives easy; we will later load it from a repo
    private static final TaskList taskList = new TaskList(new TaskListName("tapas-tasks-group5"));

    private TaskList(TaskListName taskListName) {
        this.taskListName = taskListName;
        this.listOfTasks = new ListOfTasks(new LinkedList<>());
    }

    public static TaskList getTapasTaskList() {
        return taskList;
    }

    //Only the aggregate root is allowed to create new tasks and add them to the task list.
    //Note: Here we could add some sophisticated invariants/business rules that the aggregate root checks
    public Task addNewTaskWithNameAndType(Task.TaskName name, Task.TaskType type) {
        Task newTask = Task.createTaskWithNameAndType(name, type);
        this.addNewTaskToList(newTask);

        return newTask;
    }

    public Task addNewTaskWithNameAndTypeAndInput(Task.TaskName name, Task.TaskType type, Task.InputData input) {
        Task newTask = Task.createTaskWithNameAndTypeAndInput(name, type, input);
        this.addNewTaskToList(newTask);

        return newTask;
    }

    public boolean addExistingTaskToTaskList(Task existingTask) {
        listOfTasks.value.add(existingTask);
        return true;
    }

    private void addNewTaskToList(Task newTask) {
        //Here we would also publish a domain event to other entities in the core interested in this event.
        //However, we skip this here as it makes the core even more complex (e.g., we have to implement a light-weight
        //domain event publisher and subscribers (see "Implementing Domain-Driven Design by V. Vernon, pp. 296ff).
        listOfTasks.value.add(newTask);
        //This is a simple debug message to see that the task list is growing with each new request
        LOGGER.info("Number of tasks: " + listOfTasks.value.size());
    }

    public Task retrieveTaskById(Task.TaskId id)
        throws TaskNotFoundError {

        for (Task task : listOfTasks.value) {
            if (task.getTaskId().getValue().equalsIgnoreCase(id.getValue())) {
                return task;
            }
        }
        throw new TaskNotFoundError();
    }

    public boolean changeTaskStatusToAssigned(Task.TaskId id, Task.ServiceProvider serviceProvider)
            throws TaskNotFoundError {
        return changeTaskStatus(id, new Task.TaskStatus(Task.Status.ASSIGNED), serviceProvider, null);
    }

    public boolean changeTaskStatusToRunning(Task.TaskId id, Task.ServiceProvider serviceProvider)
            throws TaskNotFoundError {
        return changeTaskStatus(id, new Task.TaskStatus(Task.Status.RUNNING), serviceProvider, null);
    }

    public boolean changeTaskStatusToExecuted(Task.TaskId id, Task.ServiceProvider serviceProvider,
                                              Task.OutputData outputData) throws TaskNotFoundError {
        return changeTaskStatus(id, new Task.TaskStatus(Task.Status.EXECUTED), serviceProvider, outputData);
    }

    private boolean changeTaskStatus(Task.TaskId id, Task.TaskStatus status, Task.ServiceProvider serviceProvider,
            Task.OutputData outputData) throws TaskNotFoundError {
        Task task = retrieveTaskById(id);
        task.setTaskStatus(status);
        task.setOutputData(outputData);

        if (serviceProvider != null) {
            task.setServiceProvider(serviceProvider);
        }

        return true;
    }

    @Value
    public static class TaskListName {
        private String value;
    }

    @Value
    public static class ListOfTasks {
        private List<Task> value;
    }
}
