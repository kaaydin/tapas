package ch.unisg.tapastasks.tasks.adapter.in.web;

import ch.unisg.tapastasks.tasks.adapter.in.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.adapter.in.web.AddNewTaskToTaskListWebController;
import ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb.TaskRepository;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListUseCase;
import ch.unisg.tapastasks.tasks.domain.Task;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AddNewTaskToTaskListWebController.class)
public class AddNewTaskToTaskListWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddNewTaskToTaskListUseCase addNewTaskToTaskListUseCase;

    @MockBean
    TaskRepository taskRepository;

    @Test
    void testAddNewTaskToTaskList() throws Exception {

        String taskName = "test-request";
        String originalTaskUri = "test-request-original-task-uri";
        String taskType = "test-request-type";
        String jsonPayLoad = new JSONObject()
            .put("taskName", taskName )
            .put("taskType", taskType)
            .put("originalTaskUri", originalTaskUri)
            .toString();

        Task taskStub = Task.createTaskWithNameAndType(new Task.TaskName(taskName),
            new Task.TaskType(taskType));

        AddNewTaskToTaskListCommand addNewTaskToTaskListCommand = new AddNewTaskToTaskListCommand(
            new Task.TaskName(taskName), new Task.OriginalTaskUri(originalTaskUri), new Task.TaskType(taskType), null
        );

        Mockito.when(addNewTaskToTaskListUseCase.addNewTaskToTaskList(addNewTaskToTaskListCommand))
            .thenReturn(taskStub.getTaskId().getValue());

        mockMvc.perform(post("/tasks/")
                .contentType(TaskJsonRepresentation.MEDIA_TYPE)
                .content(jsonPayLoad))
                .andExpect(status().isCreated());

        then(addNewTaskToTaskListUseCase).should()
            .addNewTaskToTaskList(eq(new AddNewTaskToTaskListCommand(
                new Task.TaskName(taskName), new Task.OriginalTaskUri(originalTaskUri), new Task.TaskType(taskType), null)
            ));

    }


}
