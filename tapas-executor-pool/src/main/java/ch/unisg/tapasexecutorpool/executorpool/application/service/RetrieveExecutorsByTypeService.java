package ch.unisg.tapasexecutorpool.executorpool.application.service;

import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorsByTypeQuery;
import ch.unisg.tapasexecutorpool.executorpool.application.port.in.RetrieveExecutorsByTypeUseCase;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("RetrieveExecutorsByType")
public class RetrieveExecutorsByTypeService implements RetrieveExecutorsByTypeUseCase {

    private final LoadExecutorPort loadExecutorPort;

    @Override
    public List<Executor> retrieveExecutorsByType(RetrieveExecutorsByTypeQuery query) {

        List<Executor> results = loadExecutorPort.loadExecutorsByType(query.getExecutorType());
        return results;
    }
}
