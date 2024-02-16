package ch.unisg.tapasexecutorpool.executorpool.application.service;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolQuery;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorFromExecutorPoolUseCase;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("RetrieveExecutorFromExecutorPool")
public class RetrieveExecutorFromExecutorPoolService implements RetrieveExecutorFromExecutorPoolUseCase {
    private final LoadExecutorPort loadExecutorFromRepositoryPort;
    private static final Logger LOGGER = LogManager.getLogger(RetrieveExecutorFromExecutorPoolService.class);

    @Override
    public Executor retrieveExecutorFromExecutorPool(RetrieveExecutorFromExecutorPoolQuery query) throws ExecutorNotFoundError {
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();

        try {
            Executor executor = executorPool.retrieveExecutorById(query.getExecutorId());
            return executor;
        } catch (ExecutorNotFoundError e) {
            LOGGER.info("No Executor found in cache, so try database");
            Executor executor = loadExecutorFromRepositoryPort.loadExecutor(query.getExecutorId());
            if(executor!=null){
                return executor;
            }else{
                throw new ExecutorNotFoundError();
            }
        }
    }
}