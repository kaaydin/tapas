package ch.unisg.tapastasks.tasks.adapter.in.formats;

import ch.unisg.tapastasks.tasks.domain.Task;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class is used to process JSON Patch operations for tasks: given a
 * <a href="http://jsonpatch.com/">JSON Patch</a> for updating the representational state of a task,
 * this class provides methods for extracting various operations of interest for our domain (e.g.,
 * changing the status of a task).
 */
public class TaskJsonPatchRepresentation {
    public static final String MEDIA_TYPE = "application/json-patch+json";

    private final JsonNode patch;

    /**
     * Constructs the JSON Patch representation.
     *
     * @param patch a JSON Patch as JsonNode
     */
    public TaskJsonPatchRepresentation(JsonNode patch) {
        this.patch = patch;
    }

    /**
     * Extracts the first task status replaced in this patch.
     *
     * @return the first task status changed in this patch or an empty {@link Optional} if none is
     * found
     */
    public Optional<Task.Status> extractFirstTaskStatusChange() {
        Optional<JsonNode> status = extractFirst(node ->
            isPatchReplaceOperation(node) && hasPath(node, "/taskStatus")
        );

        if (status.isPresent()) {
            String taskStatus = status.get().get("value").asText();
            return Optional.of(Task.Status.valueOf(taskStatus));
        }

        return Optional.empty();
    }

    /**
     * Extracts the first service provider added or replaced in this patch.
     *
     * @return the first service provider changed in this patch or an empty {@link Optional} if none
     * is found
     */
    public Optional<Task.ServiceProvider> extractFirstServiceProviderChange() {
        Optional<JsonNode> serviceProvider = extractFirst(node ->
            (isPatchReplaceOperation(node) || isPatchAddOperation(node))
                && hasPath(node, "/serviceProvider")
        );

        return (serviceProvider.isEmpty()) ? Optional.empty()
            : Optional.of(new Task.ServiceProvider(serviceProvider.get().get("value").asText()));
    }

    /**
     * Extracts the first output data addition in this patch.
     *
     * @return the output data added in this patch or an empty {@link Optional} if none is found
     */
    public Optional<Task.OutputData> extractFirstOutputDataAddition() {
        Optional<JsonNode> output = extractFirst(node ->
            isPatchAddOperation(node) && hasPath(node, "/outputData")
        );

        return (output.isEmpty()) ? Optional.empty()
            : Optional.of(new Task.OutputData(output.get().get("value").asText()));
    }

    private Optional<JsonNode> extractFirst(Predicate<? super JsonNode> predicate) {
        Stream<JsonNode> stream = StreamSupport.stream(patch.spliterator(), false);
        return stream.filter(predicate).findFirst();
    }

    private boolean isPatchAddOperation(JsonNode node) {
        return isPatchOperationOfType(node, "add");
    }

    private boolean isPatchReplaceOperation(JsonNode node) {
        return isPatchOperationOfType(node, "replace");
    }

    private boolean isPatchOperationOfType(JsonNode node, String operation) {
        return node.isObject() && node.get("op") != null
            && node.get("op").asText().equalsIgnoreCase(operation);
    }

    private boolean hasPath(JsonNode node, String path) {
        return node.isObject() && node.get("path") != null
            && node.get("path").asText().equalsIgnoreCase(path);
    }
}
