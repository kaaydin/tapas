package ch.unisg.tapas.auctionhouse.adapter.common.formats;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.UUID;

public class BidJsonRepresentation {
    public static final String MEDIA_TYPE = "application/json";

    @Getter
    @Setter
    private String auctionId;

    @Getter
    @Setter
    private String bidderName;

    @Getter
    @Setter
    private String auctionHouseUri;

    @Getter
    @Setter
    private String taskListUri;


    public BidJsonRepresentation() {
    }

    public BidJsonRepresentation(String auctionId, String bidderName, String auctionHouseUri, String taskListUri) {
        this.auctionId = auctionId;
        this.bidderName = bidderName;
        this.auctionHouseUri = auctionHouseUri;
        this.taskListUri = taskListUri;

    }

    public BidJsonRepresentation(Bid bid) {
        this.auctionId = bid.getAuctionId().getValue();
        this.bidderName = bid.getBidderName().getValue();
        this.auctionHouseUri = bid.getAuctionHouseUri().getValue().toString();
        this.taskListUri = bid.getTaskListUri().getValue().toString();
    }

    public static String serialize(Bid bid) throws JsonProcessingException {
        BidJsonRepresentation representation = new BidJsonRepresentation(bid);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(representation);
    }

    public static Bid deserialize(String bidString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(bidString);

        BidJsonRepresentation representation = mapper.treeToValue(data, BidJsonRepresentation.class);

        return new Bid(
            new Auction.AuctionId(representation.getAuctionId()),
            new Bid.BidderName(representation.getBidderName()),
            new Bid.AuctionHouseUri(URI.create(representation.getAuctionHouseUri())),
            new Bid.TaskListUri(URI.create(representation.getTaskListUri()))
        );
    }
}
