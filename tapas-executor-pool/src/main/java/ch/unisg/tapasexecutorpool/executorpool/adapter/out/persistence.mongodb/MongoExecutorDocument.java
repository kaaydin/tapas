package ch.unisg.tapasexecutorpool.executorpool.adapter.out.persistence.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "executors")
public class MongoExecutorDocument {
    @Id
    public String executorId;
    public String executorName;
    public String executorType;
    public String executorBaseUri;
    public String executorPoolName;

    public MongoExecutorDocument(String executorId, String executorName, String executorType, String executorBaseUri, String executorPoolName) {
        this.executorId = executorId;
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorBaseUri = executorBaseUri;
        this.executorPoolName = executorPoolName;

    }
}