package ch.unisg.tapasroster.roster.adapter.out.persistence.mongodb;

import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;
import ch.unisg.tapasroster.roster.domain.TaskAssignment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TaskAssignmentMapper {

    public TaskAssignment mapToDomainEntity(MongoTaskAssignmentDocument taskAssignmentDocument) {
        return TaskAssignment.createAssignment(
            UUID.fromString(taskAssignmentDocument.getTaskAssignmentId()),
            new ExecutorBaseUri(taskAssignmentDocument.executorBaseUri),
            taskAssignmentDocument.getTaskLocation()
        );
    }

    public List<TaskAssignment> mapToDomainEntities(List<MongoTaskAssignmentDocument> mongoTaskAssignmentDocumentList) {
        return mongoTaskAssignmentDocumentList
                .stream()
                .map(this::mapToDomainEntity)
                .collect(Collectors.toList());
    }

    public MongoTaskAssignmentDocument mapToMongoDocument(TaskAssignment taskAssignment) {
        return new MongoTaskAssignmentDocument(
                taskAssignment.getTaskAssignmentId().toString(),
                taskAssignment.getExecutorBaseUri().toString(),
                taskAssignment.getTaskLocation()
        );
    }
}
