package ch.unisg.tapas.auctionhouse.adapter.out.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.out.bids.PlaceBidForAuctionPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class is a template for implementing an HTTP adapter that places bid.
 */
@Component
@Profile("http-websub")
public class PlaceBidForAuctionHttpAdapter implements PlaceBidForAuctionPort {
    private static final Logger LOGGER = LogManager.getLogger(PlaceBidForAuctionHttpAdapter.class);

    @Override
    public void placeBid(Auction auction, Bid bid) {
        URI requestUri = URI.create(auction.getAuctionHouseUri().getValue().toString() + "auctions/bids/");
        String payload = "";
        try {
            payload = BidJsonRepresentation.serialize(bid);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(requestUri)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();

        LOGGER.info("Payload Out: " + payload);

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("Response: " + response.body());
        } catch (Exception e) {
            LOGGER.error("Error sending request: " + e.getMessage());
        }

    }
}
