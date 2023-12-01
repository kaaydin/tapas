# Dedicated service for Roster Domain

Date: 2023-09-28

## Status

Accepted

## Context

In our TAPAS application, we use domain-partitioning to guide the overall structure. 

In particular, we use the following 5 domains, which were established during the event storming at the beginning of the semester: 

- Task List ([ADR03](0003-dedicated-service-for-task-list-domain.md))
- Roster Domain ([ADR04](0004-dedicated-service-for-roster-domain.md)) - this ADR
- Auction Domain ([ADR05](0005-dedicated-service-for-auction-domain.md))
- Executor Domain ([ADR06](0006-dedicated-service-for-executor-domain.md))
- Executor Pool Domain ([ADR07](0007-dedicated-service-for-executor-pool-domain.md))


The central requirement of the Roster Domain is the assignment of tasks to the Executors. The Roster service is central as it coordinates amongst the Executors (Pool), Task List, and the Auction Domain. Once a task is created by the user, the Roster service assigns the task to an executor, either from the Executor Pool or the Auction. 

## Decision

We will implement the Roster Domain as a dedicated service within the TAPAS application. Given the user requirements, the Domain Events, and the Event Triggers we brainstormed, we decided to isolate all matching and assignment activities between Tasks and Executors to a single domain. This aligns with Granularity Disintegrators and Integrators. Because these activities do not fully fit into the Task or Executor Pool Domains. Isolating them to their own domain helps the executor and task logic to be isolated and hence the matching logic to exist separately as well. 

This decision offers the following advantages and disadvantages:

### Advantages:

- Focus on Coordination: Centralizing task-to-executor coordination simplifies this middleware functionality.

### Disadvantages:

- Development Complexity: Introducing a dedicated service for matching and assignment logic increases initial development complexity (e.g., interservice communication, data consistency)
- Service Centrality: Under the current architecture, the Roster communicates with all the other services and is at the center of the application. This lowers fault tolerance across the application as if this service has issues, it will impact the whole application. 

## Consequences

Implemented as a dedicated service, the Roster service needs to communicate with other services (e.g., Executor Pool, Task List, Auction) to assign tasks to executors and start implementation of tasks.
