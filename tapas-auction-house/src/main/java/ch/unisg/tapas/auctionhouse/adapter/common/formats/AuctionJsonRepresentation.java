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
 */
public class AuctionJsonRepresentation {
    public static final String MEDIA_TYPE = "application/json";

    @Getter @Setter
    private String auctionId;

    @Getter @Setter
    private String auctionHouseUri;

    @Getter @Setter
    private String taskUri;

    @Getter @Setter
    private String taskType;

    @Getter @Setter
    private Integer deadline;

    @Getter @Setter
    private String auctionFeedId;

    @Getter @Setter
    private String inputData;

    @Getter @Setter
    private Auction.Status status;

    public AuctionJsonRepresentation() {  }

    public AuctionJsonRepresentation(String auctionId, String auctionHouseUri, String taskUri,
            String taskType, Integer deadline, Auction.Status status, String auctionFeedId, String inputData) {
        this.auctionId = auctionId;
        this.auctionHouseUri = auctionHouseUri;
        this.taskUri = taskUri;
        this.taskType = taskType;
        this.deadline = deadline;
        this.status = status;
        this.auctionFeedId = auctionFeedId;
        this.inputData = inputData;
    }

    public AuctionJsonRepresentation(Auction auction) {
        this.auctionId = auction.getAuctionId().getValue();
        this.auctionHouseUri = auction.getAuctionHouseUri().getValue().toString();
        this.taskUri = auction.getTaskUri().getValue().toString();
        this.taskType = auction.getTaskType().getValue();
        this.deadline = auction.getDeadline().getValue();
        this.status = auction.getAuctionStatus().getValue();
        this.auctionFeedId = auction.getAuctionFeedId().getValue();
        this.inputData = auction.getInputData().getValue();
    }

    public static String serialize(Auction auction) throws JsonProcessingException {
        AuctionJsonRepresentation representation = new AuctionJsonRepresentation(auction);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(representation);
    }

    public static Auction deserialize(String auctionString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(auctionString);

        AuctionJsonRepresentation representation = mapper.treeToValue(data, AuctionJsonRepresentation.class);

        return new Auction(new Auction.AuctionId(representation.getAuctionId()),
            new Auction.AuctionHouseUri(URI.create(representation.getAuctionHouseUri())),
            new Auction.AuctionedTaskUri(URI.create(representation.getTaskUri())),
            new Auction.AuctionedTaskType(representation.getTaskType()),
            new Auction.AuctionDeadline(representation.getDeadline()),
            new Auction.AuctionStatus(representation.getStatus()),
            new Auction.AuctionFeedId(representation.getAuctionFeedId()),
            new Auction.InputData(representation.getInputData())
        );
    }
}
