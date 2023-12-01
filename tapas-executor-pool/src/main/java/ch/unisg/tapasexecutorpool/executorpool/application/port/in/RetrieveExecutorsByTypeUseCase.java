package ch.unisg.tapasexecutorpool.executorpool.application.port.in;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import java.util.List;

public interface RetrieveExecutorsByTypeUseCase {
    List<Executor> retrieveExecutorsByType(RetrieveExecutorsByTypeQuery query);
}