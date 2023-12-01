package ch.unisg.tapas.auctionhouse.adapter.out.messaging.websub;

import ch.unisg.tapas.auctionhouse.application.port.out.feeds.SubscribeToAuctionFeedPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ch.unisg.tapas.common.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Component
@Profile("http-websub")
public class SubscribeToAuctionFeedWebSubAdapter implements SubscribeToAuctionFeedPort {
    private static final Logger LOGGER = LogManager.getLogger(SubscribeToAuctionFeedWebSubAdapter.class);

    @Autowired
    private ConfigProperties config;

    @Override
    public void subscribeToFeed(Auction.AuctionFeedId feedId) {
        LOGGER.info("Subscribing to auction feed via WebSub: " + feedId.getValue());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(feedId.getValue()))
            .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            LOGGER.error("Error subscribing to auction feed via WebSub: " + e.getMessage());
        }

        if (response != null) {
            LOGGER.info("WebSub subscribe response: " + response.headers());
            // Parse the link header to retrieve the WebSub hub URL
            response.headers().map().get("Link").stream()
                .filter(link -> link.contains("rel=\"hub\""))
                .forEach(link -> {
                    String hubUrl = link.split(";")[0].replace("<", "").replace(">", "");
                    LOGGER.info("WebSub hub URL: " + hubUrl);

                    // 2. Send a request to the WebSub hub to subscribe to the auction feed
                    HttpRequest subscribeRequest = HttpRequest.newBuilder()
                        .uri(URI.create(hubUrl))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString("hub.mode=subscribe&hub.topic=" + feedId.getValue() + "&hub.callback=" + config.getWebSubSelf() + "notifications/"))
                        .build();
                    // log request body for debugging
                    LOGGER.info("WebSub subscribe request body: " + subscribeRequest.bodyPublisher().toString());
                    try {
                        HttpResponse<String> subscribeResponse = client.send(subscribeRequest, HttpResponse.BodyHandlers.ofString());
                        LOGGER.info("WebSub subscribe response: " + subscribeResponse);
                    } catch (Exception e) {
                        LOGGER.error("Error subscribing to auction feed via WebSub: " + e.getMessage());
                    }
                });
        }
    }
}
