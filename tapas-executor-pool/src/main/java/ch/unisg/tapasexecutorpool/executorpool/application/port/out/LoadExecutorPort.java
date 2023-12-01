package ch.unisg.tapasexecutorpool.executorpool.application.port.out;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;

import java.util.List;

public interface LoadExecutorPort {

    Executor loadExecutor(String executorId) throws ExecutorNotFoundError;

    List<Executor> loadExecutorsByType(String executorType);

}
