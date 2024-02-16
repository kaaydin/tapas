# Dedicated service for Auction Domain

Date: 2023-09-27

## Status

Accepted

## Context

In our TAPAS application, we use domain-partitioning to guide the overall structure. 

In particular, we use the following 5 domains, which were established during the event storming at the beginning of the semester: 

- Task List ([ADR03](0003-dedicated-service-for-task-list-domain.md))
- Roster Domain ([ADR04](0004-dedicated-service-for-roster-domain.md))
- Auction Domain ([ADR05](0005-dedicated-service-for-auction-domain.md)) - this ADR
- Executor Domain ([ADR06](0006-dedicated-service-for-executor-domain.md))
- Executor Pool Domain ([ADR07](0007-dedicated-service-for-executor-pool-domain.md))

The central requirement of the Auction House is conducting auctions to find suitable external executors when internal capabilities are lacking.

## Decision

We will implement the Auction Domain as a dedicated service within the TAPAS application. This decision is informed by our understanding of the user requirements, Domain events, and Event triggers. There is the need to interface with an external service in case our local system does not have enough resources to accomplish a task or in case we can provide some idle resources to external systems. Due to the complexity of communicating with external TAPAS applications, this logic is placed in its own Auction Domain to limit its impact on the rest of the internal application logic. We believe this aligns with the Granularity Integrators and Disintegrators by helping separate internal and external concerns. 

This decision offers the following advantages and disadvantages:

### Advantages:

- Separation of Concerns: Due to the added complexity of communicating with external systems. Having this logic isolated, allows us to develop our internal Task Execution logic and have the Auction Domain function as a transalator between our implementation and other external systems. 

### Disadvantages:

- Depending on the complexity of coordinating with external systems, the Auction Domain may have to handle a substantial amount of functionalities that may interfere with our current separation of concerns. 

## Consequences

Implemented as a dedicated service, the Auction service needs to communicate with other services (e.g., the Roster Service) to receive information about tasks, for which there were no internal executors. 
