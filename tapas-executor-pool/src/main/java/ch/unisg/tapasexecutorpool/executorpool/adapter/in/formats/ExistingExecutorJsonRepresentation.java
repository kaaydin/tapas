package ch.unisg.tapasexecutorpool.executorpool.adapter.in.formats;

import ch.unisg.tapasexecutorpool.executorpool.domain.Executor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

final public class ExistingExecutorJsonRepresentation {
    public static final String MEDIA_TYPE = "application/executor+json";

    @NotNull
    @Getter @Setter
    private String executorId;

    @NotNull
    @Getter
    private final String executorName;

    @NotNull
    @Getter
    private final String executorType;

    @NotNull
    @Getter
    private final String executorBaseUri;

    /**
     * Instantiate a executor representation with a name, type and base URI.
     *
     * @param executorName the name of the executor
     * @param executorType the type of the executor
     * @param executorBaseUri the base URI of the executor
     */
    public ExistingExecutorJsonRepresentation(String executorName, String executorType, String executorBaseUri) {
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorBaseUri = executorBaseUri;
    }

    /**
     * Instantiate a executor representation from a domain concept.
     *
     * @param executor see {@link Executor}
     */
    public ExistingExecutorJsonRepresentation(Executor executor) {
        this.executorId = executor.getExecutorId();
        this.executorName = executor.getExecutorName();
        this.executorType = executor.getExecutorType();
        this.executorBaseUri = executor.getExecutorBaseUri();
    }

    /**
     * Convenience method used to serialize a task provided as a domain concept in the format exposed
     * through the uniform HTTP API.
     *
     * @param executor the executor as defined in the domain
     * @return a string serialization using the JSON-based representation format defined for tasks
     * @throws JsonProcessingException if a runtime exception occurs during object serialization
     */
    public static String serialize(Executor executor) throws JsonProcessingException {
        ExistingExecutorJsonRepresentation representation = new ExistingExecutorJsonRepresentation(executor);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(representation);
    }
}
