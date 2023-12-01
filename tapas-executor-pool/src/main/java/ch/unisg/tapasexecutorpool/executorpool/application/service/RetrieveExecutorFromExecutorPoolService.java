package ch.unisg.tapasexecutorpool.executorpool.application.service;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolQuery;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolUseCase;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("RetrieveExecutorFromExecutorPool")
public class RetrieveExecutorFromExecutorPoolService implements RetrieveExecutorFromExecutorPoolUseCase {
    private final LoadExecutorPort loadExecutorFromRepositoryPort;

    @Override
    public Executor retrieveExecutorFromExecutorPool(RetrieveExecutorFromExecutorPoolQuery query) throws ExecutorNotFoundError {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();

        try {
            Executor executor = executorPool.retrieveExecutorById(query.getExecutorId());
            return executor;
        } catch (ExecutorNotFoundError e) {
            return loadExecutorFromRepositoryPort.loadExecutor(query.getExecutorId());
        }
    }
}