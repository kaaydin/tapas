package ch.unisg.tapasexecutorpool.executorpool.adapter.web;

import ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolCommand;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolUseCase;
import ch.unisg.tapasexecutorpool.executorpool.adapter.in.web.AddNewExecutorToExecutorPoolWebController;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb.ExecutorRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AddNewExecutorToExecutorPoolWebController.class)
public class AddNewExecutorToExecutorPoolWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddNewExecutorToExecutorPoolUseCase addNewExecutorToExecutorPoolUseCase;

    @MockBean
    ExecutorRepository executorRepository;

    @Test
    void testAddNewExecutorToExecutorPool() throws Exception {

        String executorId = UUID.randomUUID().toString();
        String executorName = "test-name";
        String executorType = "GREETING";
        String executorBaseUri = "test-base-uri";
        String jsonPayLoad = new JSONObject()
                .put("executorId", executorId)
                .put("executorName", executorName)
                .put("executorType", executorType)
                .put("executorBaseUri", executorBaseUri)
                .toString();

        Executor executorStub = Executor.createExecutorWithNameAndTypeAndBaseUri(executorName, executorType, executorBaseUri);

        AddNewExecutorToExecutorPoolCommand addNewExecutorToExecutorPoolCommand = new AddNewExecutorToExecutorPoolCommand(executorName, executorType, executorBaseUri);

        Mockito.when(addNewExecutorToExecutorPoolUseCase.addNewExecutorToExecutorPool(addNewExecutorToExecutorPoolCommand))
            .thenReturn(executorStub.getExecutorId());

        mockMvc.perform(post("/executors/")
                .contentType(ExecutorJsonRepresentation.MEDIA_TYPE)
                .content(jsonPayLoad))
                .andExpect(status().isCreated());

        then(addNewExecutorToExecutorPoolUseCase).should()
            .addNewExecutorToExecutorPool(eq(addNewExecutorToExecutorPoolCommand));

    }


}
