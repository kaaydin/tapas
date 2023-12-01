package ch.unisg.tapas.auctionhouse.adapter.out.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.application.port.out.auctions.AuctionWonEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionWonEvent;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ch.unisg.tapas.common.ConfigProperties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ch.unisg.tapas.auctionhouse.domain.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * This class is a template for sending auction won events via MQTT. This class was created here only
 * as a placeholder, it is up to you to decide how such events should be sent.
 */

@Component
@Profile("mqtt")
public class AuctionWonEventMqttAdapter implements AuctionWonEventPort {
    private static final Logger LOGGER = LogManager.getLogger(AuctionWonEventMqttAdapter.class);
    private final ConfigProperties configProperties;
    public AuctionWonEventMqttAdapter(ConfigProperties configProperties) {this.configProperties = configProperties;}
    @Override
    public void publishAuctionWonEvent(AuctionWonEvent event) {
        Bid winningBid = event.getWinningBid();

        try {
            Auction auction = AuctionRegistry
                .getInstance()
                .getAuctionById(winningBid.getAuctionId());

            LOGGER.info(String.format("After try function"));
            LOGGER.info(String.format(auction.getTaskUri().getValue().toString()));
            LOGGER.info(String.format(winningBid.getTaskListUri().getValue().toString()));
            LOGGER.info(String.format(auction.getTaskType().getValue().toString()));
            LOGGER.info(String.format(auction.getAuctionHouseUri().getValue().toString()));

            // TODO: test if works
            notifyTaskList(auction.getTaskUri(), winningBid, auction.getTaskType(), auction.getAuctionHouseUri());
        } catch (AuctionNotFoundError e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void notifyTaskList(Auction.AuctionedTaskUri taskUri, Bid winningBid, Auction.AuctionedTaskType taskType, Auction.AuctionHouseUri auctionHouseUri) {

        String inputData = getInputData(taskUri.getValue().toString());

        // Creating a JSON Object
        JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("taskName", auctionHouseUri.getValue().toString());
        jsonRequestBody.put("originalTaskUri", taskUri.getValue().toString());
        jsonRequestBody.put("taskType", taskType.getValue().toString());
        jsonRequestBody.put("inputData", inputData);
        // Output the JSON string
        String requestBody = jsonRequestBody.toString();
        System.out.println(requestBody);

        String externalTaskListUri = winningBid.getTaskListUri().getValue().toString();

        System.out.println("Request Body to " + requestBody);
        System.out.println("Request Body to " + requestBody.toString());

        WebClient.create()
            .post()
            .uri(externalTaskListUri + "tasks/")
            .body(BodyInserters.fromValue(requestBody)) // Insert JSON string as the body
            .header("Content-Type", "application/task+json") // Set content type to JSON
            .retrieve()
            .bodyToMono(String.class) // or another class as per your response
            .subscribe(
                response -> System.out.println("Response: " + response), // Handle successful response
                error -> System.out.println("Error occurred: " + error.getMessage()) // Handle error
            );
    }

    private String getInputData(String taskLocation) {
        RestTemplate restTemplate = new RestTemplate();
        String result = "";
        try {
            result = restTemplate.getForObject(taskLocation,String.class);
        } catch (RestClientException e){
            System.out.println("Failed to get the Object on URL: " + taskLocation);
            System.out.println(e.getMessage());
            throw e;
        }

        String inputData = new JSONObject(result).getString("inputData");

        return inputData;
    }

}
