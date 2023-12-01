package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedDirectoryCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.feeds.SubscribeToAuctionFeedDirectoryUseCase;
import ch.unisg.tapas.auctionhouse.application.port.out.OutputPortError;
import ch.unisg.tapas.common.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

/**
 * Controller that handles HTTP requests for subscribing to auction feeds from an Auction House Resource
 * Directory. This controller implements the {@link SubscribeToAuctionFeedDirectoryUseCase} use case
 * using the {@link SubscribeToAuctionFeedDirectoryCommand} command.
 */
@RestController
@Profile("http-websub")
@RequiredArgsConstructor
public class SubscribeToAuctionFeedDirectoryWebController {
    private static final Logger LOGGER = LogManager.getLogger(SubscribeToAuctionFeedDirectoryWebController.class);

    private final SubscribeToAuctionFeedDirectoryUseCase subscribeToAuctionFeedDirectoryUseCase;

    @Autowired
    private ConfigProperties config;

    /**
     * Handles an HTTP POST request for subscribing to auction feeds listed in an Auction House
     * Resource Directory.
     *
     * @param payload the URI of the Auction House Resource Directory
     * @return a 200 OK status code if the subscription is successful, an error status code otherwise
     */
    @PostMapping(path = "/subscribeToDirectory/")
    public ResponseEntity<Void> subscribeToDirectory(@RequestBody(required = false) String payload) {
        URI directoryUri = null;

        try {
            directoryUri = URI.create(config.getPropertyByName("websub.discovery.directory"));
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.info("There is no default URI for the Auction House Directory");
        }

        try {
             directoryUri = URI.create(payload);
        } catch (IllegalArgumentException e) {
            LOGGER.info("The URI for the Auction House Directory in the request payload is invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            LOGGER.info("The request payload did not include a URI for the Auction House Directory");
        }

        // We have no default URI and no valid URI was passed in the request payload
        if (directoryUri == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Retrieving feeds from directory: " + directoryUri.toASCIIString());

        try {
            SubscribeToAuctionFeedDirectoryCommand command = new SubscribeToAuctionFeedDirectoryCommand(directoryUri);
            subscribeToAuctionFeedDirectoryUseCase.subscribeToDirectory(command);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OutputPortError e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
}
