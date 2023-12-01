package ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutorRepository extends MongoRepository<MongoExecutorDocument,String> {

    public MongoExecutorDocument findByExecutorId(String executorId);

    public List<MongoExecutorDocument> findByExecutorType(String executorType);
}
