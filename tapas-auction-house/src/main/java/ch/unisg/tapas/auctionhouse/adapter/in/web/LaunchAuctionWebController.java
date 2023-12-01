package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.LaunchAuctionCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.auctions.LaunchAuctionUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Controller that handles HTTP requests for launching auctions. This controller implements the
 * {@link LaunchAuctionUseCase} use case using the {@link LaunchAuctionCommand}.
 */
@RestController
@RequiredArgsConstructor
public class LaunchAuctionWebController {
    private static final Logger LOGGER = LogManager.getLogger(LaunchAuctionWebController.class);

    private final LaunchAuctionUseCase launchAuctionUseCase;

    @Autowired
    private final ConfigProperties config;

    /**
     * Handles HTTP POST requests for launching auctions.
     *
     * TODO: you are free to modify this handler as you see fit to reflect the discussions for the
     * uniform HTTP API for the auction house. You should also ensure that this handler has the exact
     * behavior you would expect from the defined uniform HTTP API (status codes, returned payload,
     * HTTP headers, etc.).
     *
     * @param payload a representation of the auction to be launched
     * @return HTTP response
     */
    @PostMapping(path = "/auctions/", consumes = AuctionJsonRepresentation.MEDIA_TYPE)
    public ResponseEntity<String> launchAuction(@RequestBody AuctionJsonRepresentation payload) {
        Auction.AuctionDeadline deadline = (payload.getDeadline() == null) ?
            null : new Auction.AuctionDeadline(payload.getDeadline());

        String inputData = "empty";

        LaunchAuctionCommand command = new LaunchAuctionCommand(
            new Auction.AuctionedTaskUri(URI.create(payload.getTaskUri())),
            new Auction.AuctionedTaskType(payload.getTaskType()),
            deadline,
            new Auction.InputData(inputData)
        );

        // This command returns the created auction. We need the created auction to be able to
        // include a representation of it in the HTTP response.
        Auction.AuctionId auctionId = launchAuctionUseCase.launchAuction(command);
        new Auction.AuctionFeedId("ch/unisg/tapas/auctions/group5/" + auctionId.getValue());
        LOGGER.info("Launched auction with identifier " + auctionId.getValue());
        System.out.print("Launched auction with identifier " + auctionId.getValue());


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.LOCATION, config.getAuctionHouseUri()
            + "auctions/" + auctionId.getValue());

        // Return a 201 Created status code and a representation of the created auction
        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }
}
