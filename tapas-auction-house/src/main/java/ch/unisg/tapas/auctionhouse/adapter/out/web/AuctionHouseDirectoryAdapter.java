package ch.unisg.tapas.auctionhouse.adapter.out.web;

import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;
import ch.unisg.tapas.auctionhouse.application.port.out.feeds.RetrieveAuctionFeedsFromDirectoryPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Auction House Resource Directory used in Week 8 to discover auction feeds.
 */
@Component
@Profile("http-websub")
public class AuctionHouseDirectoryAdapter implements RetrieveAuctionFeedsFromDirectoryPort {
    private static final Logger LOGGER = LogManager.getLogger(AuctionHouseDirectoryAdapter.class);

    @Override
    public List<Auction.AuctionFeedId> retrieveFeedsFromDirectory(URI directoryUri) throws OutputPortError {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(directoryUri).GET().build();

            HttpResponse<String> response = HttpClient.newBuilder().build()
                .send(request, HttpResponse.BodyHandlers.ofString());

            List<Auction.AuctionFeedId> auctionHouseEndpoints = extractAuctionFeedIds(response.body());
            LOGGER.info("Found auction house endpoints: " + auctionHouseEndpoints);

            return auctionHouseEndpoints;
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            throw new OutputPortError(e.getCause());
        }
    }

    /**
     * Retrieves the auction feeds of all auction houses registered with this directory.
     * @return a list of auction feed identifiers
     */
    private List<Auction.AuctionFeedId> extractAuctionFeedIds(String payload) throws JsonProcessingException {
        List<Auction.AuctionFeedId> auctionHouseEndpoints = new ArrayList<>();

        // For simplicity, here we just hard code the current representation used by our
        // resource directory for auction houses
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode data = objectMapper.readTree(payload);

        for (JsonNode node : data) {
            auctionHouseEndpoints.add(new Auction.AuctionFeedId(node.get("auction-feed").asText()));
        }

        return auctionHouseEndpoints;
    }
}
