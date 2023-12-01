package ch.unisg.tapas.auctionhouse.adapter.common.formats;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

/**
 * Used to expose a representation of the state of an auction through an interface.
 *
 * TODO: This class is only meant as a starting point when defining a uniform HTTP API for the
 * Auction House. You should modify this class as you see fit!
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
