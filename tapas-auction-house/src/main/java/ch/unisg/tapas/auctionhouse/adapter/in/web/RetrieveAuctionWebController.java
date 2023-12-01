package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveAuctionQuery;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.RetrieveAuctionUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.AuctionNotFoundError;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller that handles HTTP requests for retrieving an auction hosted by this auction house.
 * This controller implements the {@link RetrieveAuctionUseCase} use case using the
 * {@link RetrieveAuctionQuery}.
 */
@RestController
@RequiredArgsConstructor
public class RetrieveAuctionWebController {
    private final RetrieveAuctionUseCase retrieveAuctionUseCase;

    /**
     * Handles HTTP GET requests to retrieve an auction by its identifier. Note: you are free to
     * modify this handler as you see fit to reflect the discussions for the uniform HTTP API for the
     * auction house. You should also ensure that this handler has the exact behavior you would expect
     * from the defined uniform HTTP API (status codes, returned payload, HTTP headers, etc.).
     *
     * @return a representation of the auction (if found)
     */
    @GetMapping(path = "/auctions/{auctionId}")
    public ResponseEntity<String> retrieveAuction(@PathVariable("auctionId") String auctionId) {
        try {
            Auction auction = retrieveAuctionUseCase.retrieveAuction(
                new RetrieveAuctionQuery(new Auction.AuctionId(auctionId))
            );

            String representation = AuctionJsonRepresentation.serialize(auction);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(representation, responseHeaders, HttpStatus.OK);
        } catch (AuctionNotFoundError e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
