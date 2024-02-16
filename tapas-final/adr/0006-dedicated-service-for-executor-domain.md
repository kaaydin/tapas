# Dedicated service for Executor Domain

Date: 2023-09-27

## Status

Accepted

## Context

In our TAPAS application, we use domain-partitioning to guide the overall structure. 

In particular, we use the following 5 domains, which were established during the event storming at the beginning of the semester: 

- Task List ([ADR03](0003-dedicated-service-for-task-list-domain.md)) 
- Roster Domain ([ADR04](0004-dedicated-service-for-roster-domain.md))
- Auction Domain ([ADR05](0005-dedicated-service-for-auction-domain.md))
- Executor Domain ([ADR06](0006-dedicated-service-for-executor-domain.md)) - this ADR
- Executor Pool Domain ([ADR07](0007-dedicated-service-for-executor-pool-domain.md))

The central requirement of the Executor is completing tasks assigned to it through the roster and communication to the task list the output of the task. 

## Decision

We will implement the Executor Domain as a dedicated service within the TAPAS application. Given our domain events and event triggers, we decided to isolate the execution logic of each task type to the Executor Domain. Each Executor will be a copy of the Executor Domain template. This allows us to conform to the Granularity Integrators and Disintegrators, by isolating and grouping together execution logic under one Domain.

This decision offers the following advantages and disadvantages:

### Advantages:

Focused development on Execution logic leads to better executors as more attention can be placed on the logic of each task execution. 

### Disadvantages:

Interoperability requirements increase as the executors have to communicate with the Roster for task assignment, somehow with the Executor Pool to be kept track of, and with the task list to update users on the status of a given task.

## Consequences

Implemented as a dedicated service, the Executor service needs to communicate with other services (e.g., the Roster Service, Task List) to receive task input and forward task output. 
