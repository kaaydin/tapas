package ch.unisg.tapasexecutorpool.executorpool.application.service;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorsByTypeQuery;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorsByTypeUseCase;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RetrieveExecutorsByTypeService implements RetrieveExecutorsByTypeUseCase {

    private final LoadExecutorPort loadExecutorPort;

    @Override
    public List<Executor> retrieveExecutorsByType(RetrieveExecutorsByTypeQuery query){
        ExecutorPool executorPool = ExecutorPool.getTapasExecutorPool();
        List<Executor> results = executorPool.retrieveExecutorsByType(query.getExecutorType());
        if (results == null) {
            results = loadExecutorPort.loadExecutorsByType(query.getExecutorType());
        }
        return results;
    }
}
