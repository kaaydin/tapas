package ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import ch.unisg.tapasexecutorpool.executorpool.domain.ExecutorPool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ExecutorMapper {

    Executor mapToDomainEntity(MongoExecutorDocument executor) {
        return Executor.createExecutorWithNameAndTypeAndBaseUri(
                executor.executorName,
                executor.executorType,
                executor.executorBaseUri
        );
    }

    MongoExecutorDocument mapToMongoDocument(Executor executor) {

        return new MongoExecutorDocument(
                executor.getExecutorId(),
                executor.getExecutorName(),
                executor.getExecutorType(),
                executor.getExecutorBaseUri(),
                ExecutorPool.getTapasExecutorPool().getExecutorPoolName().getValue()
        );
    }

    List<Executor> mapToDomainEntities(List<MongoExecutorDocument> mongoExecutorDocuments) {
        return mongoExecutorDocuments
                .stream()
                .map(this::mapToDomainEntity)
                .toList();
    }
}
