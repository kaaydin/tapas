package ch.unisg.tapasexecutorpool.executorpool.application.service;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.AddNewExecutorToExecutorPoolCommand;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.AddExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AddNewExecutorToExecutorPoolServiceTest {

    @MockBean
    private AddExecutorPort addExecutorPort;

    @MockBean
    private LoadExecutorPort loadExecutorPort;

    @Autowired
    private AddNewExecutorToExecutorPoolService addNewExecutorToExecutorPoolService;

    @Test
    void testAddNewExecutorToExecutorPool() {
        String executorName = "test-name";
        String executorType = "GREETING";
        String executorBaseUri = "test-base-uri";

        Executor newExecutor = givenAExecutorWithNameAndTypeAndBaseUri(executorName, executorType, executorBaseUri);

        ExecutorPool executorPool = givenAnEmptyExecutorPool(ExecutorPool.getTapasExecutorPool());

        AddNewExecutorToExecutorPoolCommand command = new AddNewExecutorToExecutorPoolCommand(
                newExecutor.getExecutorName(), newExecutor.getExecutorType(), newExecutor.getExecutorBaseUri()
        );

        String addedExecutorId = addNewExecutorToExecutorPoolService.addNewExecutorToExecutorPool(command);

        // Verify the interaction with addExecutorPort
        verify(addExecutorPort, times(1)).addExecutor(any());

        // Assert the returned executor ID (you might want to adjust this based on your actual logic)
        assertThat(addedExecutorId).isNotNull();
        assertThat(executorPool.getListOfExecutors().getValue()).hasSize(1);
    }

    private ExecutorPool givenAnEmptyExecutorPool(ExecutorPool executorPool) {
        executorPool.getListOfExecutors().getValue().clear();
        return executorPool;
    }
    private Executor givenAExecutorWithNameAndTypeAndBaseUri(String executorName, String executorType, String executorBaseUri) {
        Executor executor = Mockito.mock(Executor.class);
        given(executor.getExecutorName()).willReturn(executorName);
        given(executor.getExecutorType()).willReturn(executorType);
        given(executor.getExecutorBaseUri()).willReturn(executorBaseUri);
        return executor;
    }
}
