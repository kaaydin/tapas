package ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb;

import ch.unisg.tapasexecutorpool.executorpool.application.port.out.AddExecutorPort;
import ch.unisg.tapasexecutorpool.executorpool.application.port.out.LoadExecutorPort;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorNotFoundError;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExecutorPersistenceAdapter implements
        AddExecutorPort,
        LoadExecutorPort {

    @Autowired
    private final ExecutorRepository executorRepository;

    private final ExecutorMapper executorMapper;

    @Override
    public void addExecutor(Executor executor) {
        MongoExecutorDocument mongoExecutorDocument = executorMapper.mapToMongoDocument(executor);
        executorRepository.save(mongoExecutorDocument);
    }

    @Override
    public Executor loadExecutor(String executorId) throws ExecutorNotFoundError {
        MongoExecutorDocument mongoExecutorDocument = executorRepository.findByExecutorId(executorId);
        if (mongoExecutorDocument == null) {
            throw new ExecutorNotFoundError();
        }
        Executor executor = executorMapper.mapToDomainEntity(mongoExecutorDocument);
        return executor;
    }
    @Override
    public List<Executor> loadExecutorsByType(String executorType){
        List<MongoExecutorDocument> mongoExecutorDocuments = executorRepository.findByExecutorType(executorType);
        List<Executor> executors = executorMapper.mapToDomainEntities(mongoExecutorDocuments);
        return executors;
    }
}
