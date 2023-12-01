package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<MongoTaskDocument,String> {

    public MongoTaskDocument findByTaskId(String taskId, String taskListName);

    public List<MongoTaskDocument> findByTaskListName(String taskListName);
}
