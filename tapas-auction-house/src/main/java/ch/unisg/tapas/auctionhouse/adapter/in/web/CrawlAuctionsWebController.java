package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.application.port.in.feeds.CrawlAuctionFeedsCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.feeds.CrawlAuctionFeedsUseCase;

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

import java.net.URI;
import java.util.List;

@RestController
@Profile("http-websub")
@RequiredArgsConstructor
public class CrawlAuctionsWebController {
    private static final Logger LOGGER = LogManager.getLogger(CrawlAuctionsWebController.class);

    private final CrawlAuctionFeedsUseCase crawlAuctionsUseCase;

    @Autowired
    private ConfigProperties config;

    @PostMapping(path = "/crawlAuctionHouses/")
    public ResponseEntity<Void> crawlAuctions(@RequestBody(required = false) String payload) {
        URI entryPoint;
        List<String> topics;

        entryPoint = URI.create(config.getPropertyByName("discovery.other.uri"));
        topics = config.getInterestedTopics();

        if (payload != null) {
            try {
                String[] payloadParts = payload.split(" ");
                entryPoint = URI.create(payloadParts[0]);
                LOGGER.info("Using URI from payload: " + entryPoint);

                if (payloadParts.length > 1) {
                    topics = List.of(payloadParts).subList(1, payloadParts.length);
                    LOGGER.info("Using topics from payload: " + topics);
                } else {
                    LOGGER.info("No topics specified in payload, using default topics");
                }
            } catch (IllegalArgumentException e) {
                LOGGER.error("Invalid request payload: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("Using default URI and topics for crawling");
        }

        CrawlAuctionFeedsCommand command = new CrawlAuctionFeedsCommand(entryPoint, topics);
        try {
            crawlAuctionsUseCase.crawlAuctions(command);
        } catch (OutputPortError e) {
            LOGGER.error("Error while crawling auctions: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
