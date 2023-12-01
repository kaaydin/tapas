package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.bids.ReceiveBidUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;


@Profile("http-websub")
@RestController
@RequiredArgsConstructor
public class ReceiveBidForAuctionWebController {
    private final ReceiveBidUseCase receiveBidUseCase;

    @PostMapping(path = "/auctions/{auctionId}", consumes = "application/json")
    public ResponseEntity<String> receiveBid(@PathVariable("auctionId") String auctionId, @RequestBody String payload) {
        System.out.println("Received bid for auction " + auctionId);

        // create bid from json representation
        System.out.println("Payload: " + payload);
        try {
            Bid bid = BidJsonRepresentation.deserialize(payload);
            receiveBidUseCase.receiveBid(bid);

            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (JsonProcessingException e) {
            System.out.println("Error deserializing bid: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
