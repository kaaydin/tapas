# Dedicated service for Executor Pool Domain

Date: 2023-09-27

## Status

Accepted

## Context

In our TAPAS application, we use domain-partitioning to guide the overall structure. 

In particular, we use the following 5 domains, which were established during the event storming at the beginning of the semester: 

- Task List ([ADR03](0003-dedicated-service-for-task-list-domain.md)) 
- Roster Domain ([ADR04](0004-dedicated-service-for-roster-domain.md))
- Auction Domain ([ADR05](0005-dedicated-service-for-auction-domain.md))
- Executor Domain ([ADR06](0006-dedicated-service-for-executor-domain.md))
- Executor Pool Domain ([ADR07](0007-dedicated-service-for-executor-pool-domain.md)) - this ADR

The central requirement of the Executor Pool is the management of executors. The Executor Pool is designed to be dynamic, allowing for the addition or removal of Executors as necessary. It should store current executors in the application.

## Decision

We will implement the Executor Pool Domain as a dedicated service within the TAPAS application. Given the domain events and event triggers we brainstormed, we decided to isolate all the logic that pertains to organizing tasks. In accordance with the Granularity Integrators and Disintegrators, this allows for all logic for keeping track of the system execution resources to be centralized and easily maintained. 

This decision offers the following advantages and disadvantages:

### Advantages:

- Isolated execution resource tracking and organization allows for better transparency and maintenance of executor resources in the application. Especially in the form of database implementation. 

### Disadvantages:

- Isolating these would result in added communication needs for other services to be aware of current executor resources.  

## Consequences

Implemented as a dedicated service, the Executor Pool service needs to communicate with other services (e.g., the Roster Service, Executors) to find executors based on requested capabilities. 
