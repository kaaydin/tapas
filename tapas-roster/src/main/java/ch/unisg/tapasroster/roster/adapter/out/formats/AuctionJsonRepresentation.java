package ch.unisg.tapasroster.roster.adapter.out.formats;

import lombok.Getter;
import lombok.Setter;

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
