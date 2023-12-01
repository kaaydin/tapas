package ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import javax.validation.constraints.NotNull;
import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;

final public class ExecutorJsonRepresentation {
    public static final String MEDIA_TYPE = "application/executor+json";

    @NotNull
    @Getter
    private final String executorName;

    @NotNull
    @Getter
    private final String executorType;

    @NotNull
    @Getter
    private final String executorBaseUri;

    public ExecutorJsonRepresentation(String executorName, String executorType, String executorBaseUri) {
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorBaseUri = executorBaseUri;
    }

    /**
     * Converts an Executor domain object into its corresponding JSON representation.
     * @param executor The Executor domain object.
     * @return A string representing the Executor in JSON format.
     * @throws JsonProcessingException If an error occurs while processing the JSON.
     */
    public static String serialize(Executor executor) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new ExecutorJsonRepresentation(executor.getExecutorName(), executor.getExecutorType(), executor.getExecutorBaseUri()));
    }
}
