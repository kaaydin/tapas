package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tasks")
public class MongoTaskDocument {

    @Id
    public String taskId;

    public String taskName;
    public String originalTaskUri;
    public String taskType;

    public String taskStatus;
    public String serviceProvider;

    public String inputData;
    public String outputData;

    public String taskListName;

    public MongoTaskDocument(String taskId, String taskName, String originalTaskUri, String taskType,
            String inputData, String outputData, String taskStatus, String serviceProvider, String taskListName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.originalTaskUri = originalTaskUri;
        this.taskType = taskType;
        this.inputData = inputData;
        this.outputData = outputData;
        this.taskStatus = taskStatus;
        this.serviceProvider = serviceProvider;
        this.taskListName = taskListName;
    }
}
