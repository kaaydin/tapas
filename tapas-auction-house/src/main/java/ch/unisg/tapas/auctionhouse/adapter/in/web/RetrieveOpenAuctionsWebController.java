package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveOpenAuctionsQuery;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveOpenAuctionsUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ch.unisg.tapas.common.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Collection;

/**
 * Controller that handles HTTP requests for retrieving auctions hosted by this auction house that
 * are open for bids. This controller implements the {@link RetrieveOpenAuctionsUseCase} use case
 * using the {@link RetrieveOpenAuctionsQuery}.
 */
@RestController
@RequiredArgsConstructor
public class RetrieveOpenAuctionsWebController {
    private final RetrieveOpenAuctionsUseCase retrieveAuctionListUseCase;

    @Autowired
    private ConfigProperties config;
    /**
     * Handles HTTP GET requests to retrieve the auctions that are open. Note: you are free to modify
     * this handler as you see fit to reflect the discussions for the uniform HTTP API for the
     * auction house. You should also ensure that this handler has the exact behavior you would expect
     * from the defined uniform HTTP API (status codes, returned payload, HTTP headers, etc.).
     *
     * @return a representation of a collection with the auctions that are open for bids
     */
    @GetMapping(path = "/auctions/")
    public ResponseEntity<String> retrieveOpenAuctions() {
        Collection<Auction> auctions =
            retrieveAuctionListUseCase.retrieveAuctions(new RetrieveOpenAuctionsQuery());

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        for (Auction auction : auctions) {
            AuctionJsonRepresentation representation = new AuctionJsonRepresentation(auction);
            JsonNode node = mapper.valueToTree(representation);
            array.add(node);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        responseHeaders.add(HttpHeaders.LINK, "<" + config.getWebSubHub() + ">; rel=\"hub\"");
        responseHeaders.add(HttpHeaders.LINK, "<" + config.getAuctionHouseUri() + "auctions/>; rel=\"self\"");
        responseHeaders.add(HttpHeaders.LINK, "<" + config.getAuctionHouseUri() + "auctions/>; types=\"" + config.getDiscoverySelfTopics() + "\"");
        responseHeaders.add(HttpHeaders.LINK, "<" + config.getDiscoveryNextUri() + ">; types=\"" + config.getDiscoveryNextTopics() + "\"");


        return new ResponseEntity<>(array.toString(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping(path = "/discover/")
    public ResponseEntity<String> discoverAuctions() {
        Collection<Auction> auctions =
            retrieveAuctionListUseCase.retrieveAuctions(new RetrieveOpenAuctionsQuery());

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        for (Auction auction : auctions) {
            AuctionJsonRepresentation representation = new AuctionJsonRepresentation(auction);
            JsonNode node = mapper.valueToTree(representation);
            array.add(node);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        responseHeaders.add(HttpHeaders.LINK, "<" + config.getAuctionHouseUri() + "discover/>; types=\"" + config.getDiscoverySelfTopics() + "\"");
        responseHeaders.add(HttpHeaders.LINK, "<" + config.getDiscoveryNextUri() + ">; types=\"" + config.getDiscoveryNextTopics() + "\"");

        return new ResponseEntity<>(array.toString(), responseHeaders, HttpStatus.OK);
    }
}
