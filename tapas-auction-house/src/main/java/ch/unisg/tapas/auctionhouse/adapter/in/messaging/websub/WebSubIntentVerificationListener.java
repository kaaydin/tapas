package ch.unisg.tapas.auctionhouse.adapter.in.messaging.websub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Verifies the intent to subscribe/unsubscribe to a topic (cf. WebSub protocol).
 */
@RestController
@Profile("http-websub")
public class WebSubIntentVerificationListener {
    private static final Logger LOGGER = LogManager.getLogger(WebSubIntentVerificationListener.class);

    @GetMapping(path = "/auctions/notifications/")
    public ResponseEntity<String> verifyWebSubIntent(
        @RequestParam(name = "hub.topic") String hubTopic,
        @RequestParam(name = "hub.mode") String hubMode,
        @RequestParam(name = "hub.challenge") String hubChallenge) {

        LOGGER.info("Received intent validation request to " + hubMode + " for topic " + hubTopic
           + " with challenge: " + hubChallenge);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        // To verify the intent, we just need to echo the hubChallenge in the response
        return new ResponseEntity<>(hubChallenge, responseHeaders, HttpStatus.OK);
    }
}
