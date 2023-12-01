package ch.unisg.tapasexecutorpool.executorpool.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**This is a domain entity**/
public class Executor {

    // Defining all Getters / Setters
    @Getter
    private final String executorId;

    @Getter
    private final String executorName;

    @Getter
    private final String executorType;

    @Getter
    private final String executorBaseUri;

    // Defining value objects - not for ExecutorStatus as implemented as separate class

    // Creating the class with input: executor name, type and BaseUri

    public Executor(String executorName, String executorType, String executorBaseUri) {
        this.executorId = UUID.randomUUID().toString();
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorBaseUri = executorBaseUri;
    }

    public static Executor createExecutorWithNameAndTypeAndBaseUri(String executorName, String executorType, String executorBaseUri) {
        return new Executor(executorName, executorType, executorBaseUri);
    }

}