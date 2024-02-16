# Implementing Peer-to-Peer Discovery in Auction House Microservice Using HTTP Link Headers

Date: 2023-12-03

## Status
Accepted

## Context
In the TAPAS environment, a new procedure/mechanism based on peer interactions must be introduced. 
This mechanism that operates independently of a centralized resource directory.

## Decision
We plan to establish a peer-to-peer discovery method in the TAPAS auction house microservice, utilizing HTTP Link headers. This approach involves embedding Link headers in the service's responses, pointing to URLs of various auction feeds covering distinct subjects. This will enable other auction houses to locate and interact with our service by analyzing the Link headers in the responses they get. 

Additionally, the auction house microservice will be enhanced with fresh adapters to seek out peer auction feeds that align with the preferences of our business clients.

The HTTP Link headers will be applied in accordance with the Web Linking [RFC5988](https://datatracker.ietf.org/doc/html/rfc5988) Standard wherein the topic of an auction feed will be delineated in the relationship attribute, as formalized by the task force guidelines.


## Consequences
The TAPAS auction house service requires enhancements to embed Link headers in its auction feed responses and to interpret the Link headers from other auction feeds for identifying relevant auctions. 

The implementation of adapters for finding peer auction feeds independently of a central resource directory is necessary. With the ecosystem moving towards decentralization, additional safeguards need to be established to address the potential downtime of peer services.

There's also a need for more detailed guidelines and standards concerning the format and substance of Link headers to maintain uniformity and ensure seamless interaction within the task force framework.

Given that this mechanism is based on HTTP it might not be ideal for every communication context.
