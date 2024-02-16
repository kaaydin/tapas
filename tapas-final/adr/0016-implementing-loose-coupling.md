# Implementing Loose Coupling in Executors within a Web of Things Framework

Date: 2023-12-17

## Status
Accepted

## Context
In our TAPAS system, Executors serve as interfaces within a Web of Things (WoT) environment. This environment is characterized by the multiple types of connections and devices available and the potential for frequent changes in the devices (machines) with which the Executors interact. In contrast to this, the constant in this environment is the presence of a search engine for discovering network entities and a Uniform Interface for accessing these machines, all grounded in the Thing Descriptions (TD) framework.

## Decision
We have decided to employ Thing Descriptions to achieve loose coupling of Executors. This will be done by hard-coding the search engine's location. Following this, the process involves querying the search engine for specific types and constructing requests based on the TD standard and the specific TD of the target machine.

## Consequences
This approach introduces certain limitations and responsibilities:

**Assumption of a Stable Entry Point:** We are assuming the constancy of the search engine in the network. Should this entry point fail or become inaccessible, our system may encounter issues like timed-out requests or inability to find routes. While this doesn't crash the system, it limits our automatic recovery options. Additionally, the responsibility for maintaining this stable entry point lies with the external system operators.

**Maintenance and Adaptability:** Post initial development, the system is expected to be relatively self-maintaining, provided the essential elements (discovery mechanism and relevant TDs) are in place. Although this implementation may not be the most resilient or straightforward due to the reliance on an external search engine, it excels in interoperability, extensibility, adaptability, and maintainability, crucial for navigating the dynamic WoT environment.
