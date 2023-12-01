package ch.unisg.tapasroster.roster.adapter.out.formats;

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
public class AuctionJsonRepresentation {
    public static final String MEDIA_TYPE = "application/json";

    @Getter @Setter
    private String auctionHouseUri;

    @Getter @Setter
    private String taskUri;

    @Getter @Setter
    private String taskType;

    @Getter @Setter
    private Integer deadline;


    public AuctionJsonRepresentation(String auctionHouseUri, String taskUri,
                                     String taskType, Integer deadline) {
        this.auctionHouseUri = auctionHouseUri;
        this.taskUri = taskUri;
        this.taskType = taskType;
        this.deadline = deadline;

    }
}
