package ch.unisg.tapas.auctionhouse.adapter.common.formats;

import lombok.Getter;
import lombok.Setter;

/**
 * Used to expose a representation of the state of an auction through an interface.
 */
public class TaskJsonRepresentation {
    public static final String MEDIA_TYPE = "application/json";

    @Setter @Getter
    private final String originalTaskUri;

    @Getter @Setter
    private final String taskType;

    @Setter @Getter
    private final String inputData;

    @Setter @Getter
    private final String taskName;

    public TaskJsonRepresentation(String originalTaskUri, String taskType, String inputData, String taskName) {
        this.originalTaskUri = originalTaskUri;
        this.taskType = taskType;
        this.inputData = inputData;
        this.taskName = taskName;
    }
}
