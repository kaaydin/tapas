package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEvent;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEventPort;
import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


public class AddNewTaskToTaskListServiceTest {

    private final AddTaskPort addTaskPort = Mockito.mock(AddTaskPort.class);
    private final TaskListLock taskListLock = Mockito.mock(TaskListLock.class);
    private final NewTaskAddedEventPort newTaskAddedEventPort = Mockito.mock(NewTaskAddedEventPort.class);
    private final AddNewTaskToTaskListService addNewTaskToTaskListService = new AddNewTaskToTaskListService(
        newTaskAddedEventPort, addTaskPort, taskListLock);

    @Test
    void addingSucceeds() {

        Task newTask = givenATaskWithNameAndTypeAndInput(new Task.TaskName("test-task"),
            new Task.TaskType("test-type"), new Task.InputData("1+1"));

        TaskList taskList = givenAnEmptyTaskList(TaskList.getTapasTaskList());

        AddNewTaskToTaskListCommand addNewTaskToTaskListCommand = new AddNewTaskToTaskListCommand(newTask.getTaskName(),
            null, newTask.getTaskType(), newTask.getInputData());

        String addedTaskId = addNewTaskToTaskListService.addNewTaskToTaskList(addNewTaskToTaskListCommand);

        assertThat(addedTaskId).isNotNull();
        assertThat(taskList.getListOfTasks().getValue()).hasSize(1);

        then(taskListLock).should().lockTaskList(eq(TaskList.getTapasTaskList().getTaskListName()));
        then(newTaskAddedEventPort).should(times(1))
            .publishNewTaskAddedEvent(any(NewTaskAddedEvent.class));

    }

    private TaskList givenAnEmptyTaskList(TaskList taskList) {
        taskList.getListOfTasks().getValue().clear();
        return taskList;
    }

    private Task givenATaskWithNameAndTypeAndInput(Task.TaskName taskName, Task.TaskType taskType,
            Task.InputData inputData) {
        Task task = Mockito.mock(Task.class);
        given(task.getTaskName()).willReturn(taskName);
        given(task.getTaskType()).willReturn(taskType);
        given(task.getInputData()).willReturn(inputData);
        return task;
    }

}
