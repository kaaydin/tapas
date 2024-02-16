package ch.unisg.tapas.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Used to access properties provided via application.properties
 */
@Component
public class ConfigProperties {
    @Autowired
    private Environment environment;

    /**
     * Retrieves the URI of the WebSub hub. In this project, we use a single WebSub hub, but we could
     * use multiple.
     *
     * @return the URI of the WebSub hub
     */
    public URI getWebSubHub() {
        return URI.create(environment.getProperty("websub.hub"));
    }

    public URI getWebSubSelf() {
        return URI.create(environment.getProperty("websub.self"));
    }

    /**
     * Retrieves the URI used to publish content via WebSub. In this project, we use a single
     * WebSub hub, but we could use multiple. This URI is usually different from the WebSub hub URI.
     *
     * @return URI used to publish content via the WebSub hub
     */
    public URI getWebSubPublishEndpoint() {
        return URI.create(environment.getProperty("websub.hub.publish"));
    }

    /**
     * Retrieves the URI of the Peer Auction House Feed. In this project, we use a single
     * Peer Auction House, but we could use multiple.
     *
     * @return the URI of the Peer Auction House Feed
     */
    public URI getDiscoveryNextUri() {
        return URI.create(environment.getProperty("discovery.other.uri"));
    }

    /**
     * Retrieves the peer topic advertised.
     *
     * @return the topic of auctions advertised by the peer
     */
    public String getDiscoveryNextTopics() {
        return environment.getProperty("discovery.other.topics");
    }

    /**
     * Retrieves the topic of our own auction feed.
     *
     * @return the topic of our own auction feed
     */
    public String getDiscoverySelfTopics() {
        return environment.getProperty("discovery.self.topics");
    }

    /**
     * Retrieves the list of topics we want to subscribe to.
     *
     * @return the list of topics we want to subscribe to
     */
    public List<String> getInterestedTopics() {
        String[] topics = environment.getProperty("discovery.topics").split(",");
        return List.of(topics);
    }

    /**
     * Retrieves the URI of the MQTT broker.
     *
     * @return the URI of the MQTT broker
     */
    public URI getMqttBrokerAddress() {
        return URI.create(environment.getProperty("mqtt.broker"));
    }

    /**
     * Retrieves the auction topic address.
     *
     * @return the auction topic address
     */
    public String getMqttAuctionsTopic() {
        return environment.getProperty("mqtt.topics.auctions");
    }

    /**
     * Retrieves the executor topic address.
     *
     * @return the executor topic address
     */
    public String getMqttExecutorsTopic() {
        return environment.getProperty("mqtt.topics.executors");
    }


    /**
     * Retrieves the name of the group providing this auction house.
     *
     * @return the identifier of the group, e.g. tapas-group1
     */
    public String getGroupName() {
        return environment.getProperty("group");
    }

    /**
     * Retrieves the base URI of this auction house.
     *
     * @return the base URI of this auction house
     */
    public URI getAuctionHouseUri() {
        return URI.create(environment.getProperty("auction.house.uri"));
    }

    /**
     * Retrieves the URI of the TAPAS-Tasks task list of this TAPAS applicatoin. This is used, e.g.,
     * when placing a bid during the auction (see also {@link ch.unisg.tapas.auctionhouse.domain.Bid}).
     *
     * @return Tasklist URI
     */
    public URI getTaskListUri() {
        return URI.create(environment.getProperty("tasks.list.uri"));
    }


    /**
     * Generic method to retrieve a property from the config file by name.
     *
     * @param name the name of the property
     * @return the property's value or null if the property is not defined
     */
    public String getPropertyByName(String name) {
        return environment.getProperty(name);
    }
}
