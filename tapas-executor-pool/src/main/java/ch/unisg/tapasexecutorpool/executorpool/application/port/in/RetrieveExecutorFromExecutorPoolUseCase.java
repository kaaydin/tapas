package ch.unisg.tapasexecutorpool.executorpool.application.port.in;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;

public interface RetrieveExecutorFromExecutorPoolUseCase {
    Executor retrieveExecutorFromExecutorPool(RetrieveExecutorFromExecutorPoolQuery query) throws ExecutorNotFoundError;
}