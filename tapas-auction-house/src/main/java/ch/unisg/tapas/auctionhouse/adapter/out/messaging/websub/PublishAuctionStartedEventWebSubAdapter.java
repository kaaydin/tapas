package ch.unisg.tapas.auctionhouse.adapter.out.messaging.websub;

import ch.unisg.tapas.auctionhouse.application.port.out.auctions.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;
import ch.unisg.tapas.common.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

/**
 * This class is a template for notifying a WebSub Hub that a new auction was started.
 */
@Component
@Profile("http-websub")
public class PublishAuctionStartedEventWebSubAdapter implements AuctionStartedEventPort {
    private static final Logger LOGGER = LogManager.getLogger(PublishAuctionStartedEventWebSubAdapter.class);

    // You can use this object to retrieve properties from application.properties, e.g. the
    // WebSub hub publish endpoint, etc.
    @Autowired
    private ConfigProperties config;

    @Override
    public void publishAuctionStartedEvent(AuctionStartedEvent event) {
        LOGGER.info("Publishing auction started event via WebSub");

        // send request to WebSub hub to notify new content
        HttpRequest request;
        request = HttpRequest.newBuilder()
            .uri(config.getWebSubPublishEndpoint())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .POST(HttpRequest.BodyPublishers.ofString("hub.mode=publish&hub.url=" + config.getWebSubSelf()))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("WebSub publish response: " + response.headers());
        } catch (Exception e) {
            LOGGER.error("Error publishing auction started event via WebSub: " + e.getMessage());
        }

    }
}
