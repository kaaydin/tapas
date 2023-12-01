package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListUseCase;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEvent;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEventPort;
import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.Task;

import ch.unisg.tapastasks.tasks.domain.TaskList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
@Service("AddNewTaskToTaskList")
public class AddNewTaskToTaskListService implements AddNewTaskToTaskListUseCase {

    private final NewTaskAddedEventPort newTaskAddedEventPort;

    private final AddTaskPort addTaskToRepositoryPort;

    private final TaskListLock taskListLock;


    @Override
    public String addNewTaskToTaskList(AddNewTaskToTaskListCommand command) {
        TaskList taskList = TaskList.getTapasTaskList();

        taskListLock.lockTaskList(taskList.getTaskListName());
        Task newTask;

        // The buckpal project has some stubs for locking of the Account at this point. This would be the responsibility
        // of the service that implements the use case. For our implementation of the task list there is no immediate
        // reason to lock the list during the adding of a new task.

        if (command.getTaskInput() != null) {
            newTask = taskList.addNewTaskWithNameAndTypeAndInput(command.getTaskName(), command.getTaskType(),
               command.getTaskInput());
        } else {
            newTask = taskList.addNewTaskWithNameAndType(command.getTaskName(), command.getTaskType());
        }

        if (command.getOriginalTaskUri() != null) {
            newTask.setOriginalTaskUri(command.getOriginalTaskUri());
        }

        addTaskToRepositoryPort.addTask(newTask);

        taskListLock.releaseTaskList(taskList.getTaskListName());


        //Here we are using the application service to emit an event to the outside of the bounded context.
        //This event should be considered as a light-weight "integration event" to communicate side effects to other services.

        if (newTask != null) {
            NewTaskAddedEvent newTaskAdded = new NewTaskAddedEvent(newTask.getTaskId(),
                taskList.getTaskListName(), newTask.getTaskType());
            newTaskAddedEventPort.publishNewTaskAddedEvent(newTaskAdded);
        }

        return newTask.getTaskId().getValue();
    }
}
