package ch.unisg.tapasexecutorpool.executorpool.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolQuery;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RetrieveExecutorFromExecutorPoolServiceTest {

    @Mock
    private LoadExecutorPort loadExecutorPort;

    @Mock
    private ExecutorPool executorPool;

    @InjectMocks
    private RetrieveExecutorFromExecutorPoolService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Assuming ExecutorPool is a singleton and we need to mock its static method
        // If it's not the case, you may skip this part
        mockStatic(ExecutorPool.class);
        when(ExecutorPool.getTapasExecutorPool()).thenReturn(executorPool);
    }

    @Test
    public void testRetrieveExecutorFromExecutorPoolWithFoundExecutor() throws ExecutorNotFoundError {
        // given
        Executor executor = new Executor("1","2","3");

        when(executorPool.retrieveExecutorById(executor.getExecutorId())).thenReturn(executor);

        // when
        Executor result = service.retrieveExecutorFromExecutorPool(new RetrieveExecutorFromExecutorPoolQuery(executor.getExecutorId()));

        // then
        assertEquals(executor, result);
        verify(loadExecutorPort, never()).loadExecutor(any());
    }
}