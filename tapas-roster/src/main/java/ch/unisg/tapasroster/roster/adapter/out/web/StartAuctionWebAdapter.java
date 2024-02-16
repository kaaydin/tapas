package ch.unisg.tapasroster.roster.adapter.out.web;

import ch.unisg.tapasroster.roster.adapter.out.formats.AuctionJsonRepresentation;
import ch.unisg.tapasroster.roster.application.port.out.StartAuctionPort;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class StartAuctionWebAdapter implements StartAuctionPort {
    private static final Logger LOG = Logger.getLogger(StartAuctionWebAdapter.class.getName());

    private final Environment environment;

    public StartAuctionWebAdapter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void startAuction(String taskUri, String taskType) {
        AuctionJsonRepresentation auctionJsonRepresentation = new AuctionJsonRepresentation(
            environment.getProperty("auctionhouse.uri"),
            taskUri,
            taskType,
            10000
        );
        // Launches an auction to find an external executor for our task
        invokeAuctionRequest(auctionJsonRepresentation);
    }

    private void invokeAuctionRequest(AuctionJsonRepresentation auctionJsonRepresentation) {
        WebClient.create()
                .post()
                .uri(environment.getProperty("auctionhouse.uri") + "auctions/")
                .body(Mono.just(auctionJsonRepresentation), AuctionJsonRepresentation.class)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> LOG.info(String.format("Created new auction for the task " + auctionJsonRepresentation.getTaskUri())))
                .doOnError(err -> LOG.severe(String.format("Launching an auction for task %s failed with the following reason: " + err.getMessage(), auctionJsonRepresentation.getTaskUri())))
                .onErrorComplete()
                .subscribe();
    }
}
