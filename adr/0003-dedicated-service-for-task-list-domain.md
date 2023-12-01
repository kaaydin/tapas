# Dedicated service for Task List Domain

Date: 2023-09-28

## Status

Accepted

## Context

In our TAPAS application, we use domain-partitioning to guide the overall structure. 

In particular, we use the following 5 domains, which were established during the event storming at the beginning of the semester: 

- Task List ([ADR03](0003-dedicated-service-for-task-list-domain.md)) - current ADR
- Roster Domain ([ADR04](0004-dedicated-service-for-roster-domain.md))
- Auction Domain ([ADR05](0005-dedicated-service-for-auction-domain.md))
- Executor Domain ([ADR06](0006-dedicated-service-for-executor-domain.md))
- Executor Pool Domain ([ADR07](0007-dedicated-service-for-executor-pool-domain.md))

The central requirement of the Task List Domain is the management of tasks, allowing users to modify the organization's task list (i.e., add or remove tasks) as needed.

## Decision

We will implement the Task List Domain as a dedicated service within the TAPAS application. This decision is primarily informed by our consideration of Granularity Disintegrators and Integrators. According to our Event Triggers and Domain Events, there is a subcategory of requirements dedicated to receiving tasks from the user, storing these tasks, forwarding these tasks to the application, and communicating the results of these tasks to the user. As a result of this shared logic around Tasks, we decided to create a Task List Domain to integrate these actions surrounding Tasks.  

This decision offers the following advantages and disadvantages:

### Advantages:

- Isolating the Task List from other domains separates most Task logic from other services and allows for focused implementation of Task-related logic under a single service. 

### Disadvantages:

- Introducing a dedicated service may increase development complexity (e.g., interservice communication, data consistency). This makes ensuring coordination between microservices more difficult. 

## Consequences

Implemented as a dedicated service, the Task List service needs to communicate with other services (e.g., the Roster Service and Executors) to forward task input, receive task output, and update task status. 
