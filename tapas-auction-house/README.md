# tapas-auction-house

The Auction House is the part of your TAPAS application that is largely responsible for the interactions
between the TAPAS applications developed across all groups: it is responsible for launching and managing
auctions for tasks that your own executors cannot accomplish. The Auction House is implemented following
the Hexagonal Architecture (based on examples from book "Get Your Hands Dirty on Clean Architecture" by
Tom Hombergs).

**Technologies:** Java, Spring Boot, Maven

**Note:** this repository contains an [EditorConfig](https://editorconfig.org/) file (`.editorconfig`)
with default editor settings. EditorConfig is supported out-of-the-box by the IntelliJ IDE. To help maintain
consistent code styles, we recommend to reuse this editor configuration file in all your services.

## Table of Contents
- [Project Overview](#project-overview)
- [How to Run](#how-to-run)
- [Task 1](#task-1)
- [Task 2](#task-2)
- [Task 3](#task-3)
- [Task 4](#task-4)

## Project Overview

In a nutshell, the Auction House implements the following functionality:
- it receives commands via HTTP to launch auctions (see sample HTTP request below)
- once launched, each auction has a deadline by which it is open for bids; after the deadline has passed,
  the Auction House closes the auction and selects a random bid as winner (if any)
    - note: bids are symbolic, meaning there is no transfer of value between the Auction Houses
- the Auction House is both an auctioneer and a participant: it not only launches and manages auctions,
  but it also participates in auctions launched by other groups
    - your Auction House will automatically place a bid in an auction if one of your executors can accomplish
      the task being auctioned
- in the TAPAS ecosystem, we will assume that your Auction House will never launch an auction for a task
  that can be accomplished by one of your executors; this assumption implies that your Auction House should never
  be able to bid on an auction it launches
    - however, to facilitate development and testing, your Auction House is allowed to bid on its
      own auctions; this should never happen within the TAPAS ecosystem (only during dev)

Note from the above that the Auction House needs to have an up-to-date view of the executors available in
your TAPAS application (see also [Task 1](#task-1)).

The project template we provide already implements most of the functionality required by the Auction House.
As such, your development effort will focus primarily on adapters for HTTP, WebSub, and MQTT.

The `ch.unisg.tapas.auctionhouse.adapter` package is structured as follows:

```shell
├── common
│   ├──clients   # includes an MQTT client you can use for your Auction House
│   └── formats   # here you can define representation formats for your uniform HTTP API
├── in   # input adapters
│   ├── messaging   # adapters used to receive messages via HTTP, WebSub, or MQTT
│   │   ├── http
│   │   ├── mqtt
│   │   └── websub
│   └── web   # adapters used to receive commands and queries via HTTP
└── out   # output adapters
    ├── messaging   # adapters used to send messages via HTTP, WebSub, or MQTT
    │   ├── http
    │   ├── mqtt
    │   └── websub
    └── web   # adapters used for sending commands and queries via HTTP
```

**Note:** The project template already implements several adapters you can reuse in Tasks 1-3. Note,
however, that all the adapters we provide are only meant to help you get started. You should modify
and extend these adapters (and create new ones) to reflect the discussions both inside your group and
across groups.

## How to run

The Auction House is already integrated with the CI/CD pipeline of your TAPAS application, and it is
configured to run on port `8090` by default.

### How to Run Locally

You can run the Auction House locally as a standalone service with Maven:

```shell
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.config.location=classpath:/application-local.properties
```

or simply by using the Docker compose configuration for local deployment.

### Deployment with WebSub (Task 2)

For Task 2, we will use two public [W3C WebSub](https://www.w3.org/TR/websub/) hubs: the
[Google WebSub Hub](https://websub.appspot.com/) and [Switchboard](https://switchboard.p3k.io/) (open-source).
Both hubs follow the W3C Recommendation (and can be easily interchanged) and provide detailed
reports of subscriptions (feature useful for debugging). For more details, see the documentation
on the hubs' landing pages.

To deploy the Auction House with WebSub, you need to set the Spring active profile to `http-websub`.
You can do so in the `*.properties` file. See [application.properties](src/main/resources/application.properties)
and [application-local.properties](src/main/resources/application-local.properties) for more info.

**Note:** if you run the Auction House locally, for instance at home, and you are behind a router, you will need to
set up port forwarding to make it reachable for WebSub hubs.

### Deployment with MQTT (Task 3)

For Task 3, we will use the public MQTT broker provided by [HiveMQ](http://www.mqtt-dashboard.com/).

To deploy the Auction House with MQTT, you need to set the Spring active profile to `mqtt`.
You can do so in the `*.properties` file. See [application.properties](src/main/resources/application.properties)
and [application-local.properties](src/main/resources/application-local.properties) for more info.

You can easily set up an MQTT broker for local development with HiveMQ and Docker:
[https://www.hivemq.com/downloads/docker/](https://www.hivemq.com/downloads/docker/)

```shell
docker run -p 8080:8080 -p 1883:1883 hivemq/hivemq4
```

The above command launches a Docker container with a HiveMQT broker and binds to the container on 2 ports:
* port `1883` is used by the MQTT protocol
* port `8080` is used for the HiveMQ dashboard; point your browser to: [http://localhost:8080/](http://localhost:8080/)

To bind the Docker container to a different HTTP port, you can configure the first parameter. For example,
this command binds the HiveMQT dashboard to port `8085`:

```shell
docker run -p 8085:8080 -p 1883:1883 hivemq/hivemq4
```

For development and debugging, it might help to install an MQTT client as well. HiveMQ provides an
[MQTT Command-Line Interface (CLI)](https://hivemq.github.io/mqtt-cli/) that may help. Here is an
example for sending a message via the HiveMQT MQTT CLI:

```shell
 mqtt pub -t ch/unisg/tapas-group1/executors -m '{ "taskType" : "taskType1", "executorId" : "executor1" }'
```

## Task 1

The project template provides initial implementations of adapters for (sample HTTP requests incl. below):
- launching an auction (see [LaunchAuctionWebController](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/web/LaunchAuctionWebController.java))
- retrieving an auction (see [RetrieveAuctionWebController]( src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/web/RetrieveAuctionWebController.java))
- retrieving the list of auctions open for bids (see [RetrieveOpenAuctionsWebController](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/web/RetrieveOpenAuctionsWebController.java))
- receiving an executor added event (see [ExecutorAddedEventListenerHttpAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/messaging/http/ExecutorAddedEventListenerHttpAdapter.java))

The project template does not include any adapters for receiving bids. It is up to you to decide
how this functionality is to be modelled and implemented.

### Sample HTTP request for launching an auction

```shell
curl -i --location --request POST 'https://tapas-auction-house.86-119-34-23.nip.io/auctions/' \
--header 'Content-Type: application/json' \
--data-raw '{
  "taskUri" : "http://example.org",
  "taskType" : "taskType1",
  "deadline" : 10000
}'

HTTP/2 201
date: Sun, 5 Nov 2023 18:35:12 GMT
location: https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/auctions/3
content-length: 0
```

### Sample HTTP request for retrieving an auction

```shell
curl -i --location --request GET 'https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/auctions/1'
HTTP/2 200
content-type: application/json
date: Sun, 5 Nov 2023 18:33:21 GMT
content-length: 186

{
  "auctionId":"1",
  "auctionHouseUri":"https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/",
  "taskUri":"http://example.org",
  "taskType":"taskType1",
  "deadline":10000,
  "status":"CLOSED"
}
```

### Sample HTTP request for retrieving auctions that are open for bids

```shell
curl -i --location --request GET 'https://tapas-auction-house.86-119-34-23.nip.io/auctions/'

HTTP/2 200
content-type: application/json
date: Sun, 5 Nov 2023 18:50:17 GMT
content-length: 371

[
  {
    "auctionId":"5",
    "auctionHouseUri":"https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/",
    "taskUri":"http://example.org",
    "taskType":"taskType1",
    "deadline":10000,
    "status":"OPEN"
  },
  {
    "auctionId":"4",
    "auctionHouseUri":"https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/",
    "taskUri":"http://example.org",
    "taskType":"taskType1",
    "deadline":10000,
    "status":"OPEN"
  }
]
```

### Sample HTTP request for registering an executor

```shell
curl -i --location --request POST 'https://tapas-auction-house.86-119-34-23.nip.io/executors/taskType1/executor1'

HTTP/2 204
date: Sun, 5 Nov 2023 18:38:56 GMT
```

## Task 2

To solve this task, it is important to first understand the flow of the [W3C WebSub protocol](https://www.w3.org/TR/websub/#high-level-protocol-flow).
Some questions to help guide you through your understanding of the protocol:
- What are the responsibilities of subscribers, publishers, and hubs in the WebSub protocol?
- How can a publisher advertise a WebSub hub?
- How can a publisher distribute content via a WebSub hub?
- How can a subscriber discover a WebSub hub at run time?
- How can a subscriber register with a WebSub hub?

Once you have the answers to these questions, you can start working on your implementation.
The project template includes several input adapters for:
- verifying the intent of the subscriber (cf. W3C WebSub protocol; see [WebSubIntentVerificationListener](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/messaging/websub/WebSubIntentVerificationListener.java))
- receiving an HTTP request to subscribe to an auction feed via WebSub; note you still need to implement
an output adapter to actually subscribe to the feed via WebSub (see details below)
- subscribing to auction feeds from a resource directory (see sample HTTP requests below)

In addition, the project template also includes stubs for the following WebSub adapters:
- publishing when a new auction has started (output adapter; see [PublishAuctionStartedEventWebSubAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/websub/PublishAuctionStartedEventWebSubAdapter.java))
- subscribing to an auction feed (output adapter; see [SubscribeToAuctionFeedWebSubAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/websub/SubscribeToAuctionFeedWebSubAdapter.java))
- receiving notifications when new auctions are launched (input adapter; see [AuctionStartedEventListenerWebSubAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/messaging/websub/AuctionStartedEventListenerWebSubAdapter.java))
- placing a bid for an auction via HTTP (output adapter; see [PlaceBidForAuctionHttpAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/web/PlaceBidForAuctionHttpAdapter.java))
- sending an auction won event via HTTP (output adapter; see [AuctionWonEventHttpAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/http/AuctionWonEventHttpAdapter.java))

### Sample HTTP request for subscribing to an auction feed via WebSub

```shell
curl -i --location --request POST 'https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/subscribeToFeed/' \
--header 'Content-Type: text/plain' \
--data-raw 'https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/auctions/'

HTTP/2 200
date: Sun, 5 Nov 2023 19:14:02 GMT
content-length: 0
```

**Note:** the project template only implements the HTTP adapter and application service for handling this
request. To actually subscribe to the feed, you also need to implement the output port [SubscribeToAuctionFeedWebSubAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/websub/SubscribeToAuctionFeedWebSubAdapter.java).

### Sample HTTP request for subscribing (via WebSub) to auction feeds from a resource directory

To help you bootstrap the TAPAS ecosystem with WebSub, we provide an Auction House Resource Directory
where you can register your auction feed and discover the auction feeds of other groups. For more details
on how to register/deregister your auction feed with the directory, see the API documentation on Swagger Hub:
[https://app.swaggerhub.com/apis-docs/interactions-hsg/auction-house/](https://app.swaggerhub.com/apis-docs/interactions-hsg/auction-house/)

```shell
curl -i --location --request POST 'https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/discovery/directory/' \
--header 'Content-Type: text/plain' \
--data-raw 'https://api.interactions.ics.unisg.ch/auction-houses/'

HTTP/2 200
date: Sun, 5 Nov 2023 19:24:14 GMT
content-length: 0
```

The functionality of subscribing to feeds discovered via the Auction House Resource Directory is provided
— but, just like above, to actually subscribe to the feed you also need to implement the output port [SubscribeToAuctionFeedWebSubAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/websub/SubscribeToAuctionFeedWebSubAdapter.java)).
If there is a problem with the resource directory, the Auction House returns a `503 Service Unavailable`
status code. If no directory URI is given (either in the `*.properties` file or in the request payload),
the Auction House returns a `400 Bad Request`.

## Task 3

To solve this task, it is important to first understand how the MQTT protocol works (see Lecture 8).

The project template includes:
- an MQTT client you can use in your Auction House (see [TapasMqttClient](src/main/java/ch/unisg/tapas/auctionhouse/adapter/common/clients/TapasMqttClient.java)).
- an adapter for receiving an HTTP request to subscribe to an auction feed via MQTT; note you still need to implement
an output adapter to actually subscribe to the feed via MQTT (see details below)

In addition, the project template also includes stubs for the following MQTT adapters:
- publishing when a new auction has started (output adapter; see [PublishAuctionStartedEventMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/mqtt/PublishAuctionStartedEventMqttAdapter.java))
- subscribing to an auction feed (output adapter; see [SubscribeToAuctionFeedMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/mqtt/SubscribeToAuctionFeedMqttAdapter.java))
- receiving notifications when new auctions are launched (input adapter; see [AuctionStartedEventListenerMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/messaging/mqtt/AuctionStartedEventListenerMqttAdapter.java))
- placing a bid for an auction (output adapter; see [PlaceBidForAuctionMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/mqtt/PlaceBidForAuctionMqttAdapter.java))
- sending an auction won event (output adapter; see [AuctionWonEventMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/mqtt/AuctionWonEventMqttAdapter.java))

### Sample HTTP request for subscribing to an auction feed via MQTT

To send the command to subscribe to an auction feed via MQTT, you can use the same HTTP endpoint as
for WebSub — but with an MQTT topic as payload:

```shell
curl -i --location --request POST 'https://tapas-auction-house.86-119-34-23.asse.scs.unisg.ch/subscription/' \
--header 'Content-Type: text/plain' \
--data-raw 'ch/unisg/tapas/auctions'

HTTP/2 200
date: Sun, 5 Nov 2023 20:02:28 GMT
content-length: 0
```

**Note:** the project template only implements the HTTP adapter and application service for handling
this request. To actually subscribe to the auction feed via MQTT, you also need to implement the output
port [SubscribeToAuctionFeedMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/out/messaging/mqtt/SubscribeToAuctionFeedMqttAdapter.java).

### Example of an MQTT adapter

To help you get started, we provide an example of an adapter for receiving an executor added event
via MQTT (see [ExecutorAddedEventListenerMqttAdapter](src/main/java/ch/unisg/tapas/auctionhouse/adapter/in/messaging/mqtt/ExecutorAddedEventListenerMqttAdapter.java)).

Note we intentionally choose this event as an example: this event is internal to your TAPAS application,
but in Task 3 you are primarily concerned to interact with the Auction Houses of the other groups.

## Task 4

Don't forget about Task 4! (see assignment sheet)
