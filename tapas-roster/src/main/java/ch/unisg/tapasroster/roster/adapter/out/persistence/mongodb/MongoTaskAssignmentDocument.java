package ch.unisg.tapasroster.roster.adapter.out.persistence.mongodb;

import ch.unisg.tapasroster.roster.domain.ExecutorBaseUri;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "taskAssignments")
public class MongoTaskAssignmentDocument {

    @Id
    public String taskAssignmentId;
    public String executorBaseUri;
    public String taskLocation;

    public MongoTaskAssignmentDocument(String taskAssignmentId, String executorBaseUri, String taskLocation) {
        this.taskAssignmentId = taskAssignmentId;
        this.executorBaseUri = executorBaseUri;
        this.taskLocation = taskLocation;
    }
}
