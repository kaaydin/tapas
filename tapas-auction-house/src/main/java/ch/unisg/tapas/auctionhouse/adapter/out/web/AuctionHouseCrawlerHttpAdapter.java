package ch.unisg.tapas.auctionhouse.adapter.out.web;

import ch.unisg.tapas.auctionhouse.application.port.out.feeds.RetrieveAuctionFeedsFromCrawlerPort;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("http-websub")
public class AuctionHouseCrawlerHttpAdapter implements RetrieveAuctionFeedsFromCrawlerPort {
    private static final Logger LOGGER = LogManager.getLogger(AuctionHouseCrawlerHttpAdapter.class);

    @Override
    public List<Auction.AuctionFeedId> retrieveAuctionFeedsFromCrawler(URI entryPoint, List<String> typesOfInterest)  {

        URI currentUri = entryPoint;
        List<String> queue = new ArrayList<>();
        List<String> checkedAuctions = new ArrayList<>();
        List<Auction.AuctionFeedId> auctionFeeds = new ArrayList<>();
        List<String> typesToSubscribe = new ArrayList<>(typesOfInterest);

        while (!typesToSubscribe.isEmpty()) {
            HttpHeaders linkHeaders = crawlUri(currentUri);
            if (linkHeaders == null) {
                if (queue.size() > 1) {
                    queue.remove(0);
                    currentUri = URI.create(queue.get(0));
                } else {
                    LOGGER.warn("No URI to crawl");
                    break;
                }
            } else {
                //To test
                List<String> linkHeaderValues = linkHeaders.map().get("Link");
                for (String linkHeader : linkHeaderValues) {
                    LOGGER.info("AuctionFeed of the Link: " + (linkHeader.split(";")[0].split("<")[1].split(">")[0]));
                    LOGGER.info("Tasktypes of the Link: " + linkHeader.split("=")[1].replace("\"", ""));
                    LOGGER.info("Linkheader: " + linkHeader);
                    if (linkHeader.split(";")[1].split("=")[0].contains("types")) {
                        String auctionFeedId = linkHeader.split(";")[0].split("<")[1].split(">")[0];
                        String taskTypes = linkHeader.split("=")[1].replace("\"", "");
                        String[] types = taskTypes.split(",");
                        if (linkHeader.contains(currentUri.toString())) {
                            for (String type : types) {
                                if (typesToSubscribe.contains(type)) {
                                    auctionFeeds.add(new Auction.AuctionFeedId(auctionFeedId.replace("discover", "auctions")));
                                    typesToSubscribe.remove(type);
                                    break;
                                }
                            }
                        } else if (!checkedAuctions.contains(auctionFeedId)) {
                            queue.add(auctionFeedId);
                            checkedAuctions.add(queue.get(0));
                        }
                    }
                }
                if (!queue.isEmpty()) {
                    currentUri = URI.create(queue.get(0));
                    queue.remove(0);
                } else {
                    LOGGER.warn("No more auction houses to crawl");
                    break;
                }
            }
        }
        LOGGER.info("Subscribed to the following auction houses: " + auctionFeeds);
        return auctionFeeds;
    }
    private HttpHeaders crawlUri(URI peerUri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(peerUri).GET().build();

            HttpResponse<String> response = HttpClient.newBuilder().build()
                .send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("Response headers: " + response.headers());
            return response.headers();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error while crawling: " + e.getMessage());
            return null;
        }
    }
}

