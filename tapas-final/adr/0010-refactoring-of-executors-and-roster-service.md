# Refactoring of Executors and Roster Service

Date: 2023-10-14

## Status

Accepted

## Context

As indicated in [8. Refactoring of Executors & Roster](0008-implementing-executors-and-assignment-logic-within-executor-pool-domain.md), we implemented the Executors and the Roster logic (i.e., the assignment of tasks to executors) within the Executor Pool as an initial version. However, we indicated that our plan was to separate these componenets into distinct microservices in subsequent iterations to accomodate the long-term strategy (in alignment with ADRs 3 to 7).  

## Decision

We implemented the Executors and the Roster as separate microservices. The advantages and disadvantages of such a decision was described in detail in ADRs [4. Dedicated service for Roster Domain](0004-dedicated-service-for-roster-domain.md) and [6. Dedicated service for Executor Domain](0006-dedicated-service-for-executor-domain). 

## Consequences

The decision to separate Executors and the Roster into distinct microservices aligns with the long-term architectural goals outlined in ADRs [3. Dedicated service for Task List Domain](0003-dedicated-service-for-task-list-domain.md) to [7. Dedicated service for Executor Pool Domain](0007-dedicated-service-for-executor-pool-domain.md). While it offers advantages in terms of modularity, it also introduces challenges related to increased complexity and communication needs between services. The team should plan for proper communication strategies to address these consequences effectively and ensure the success of this architectural shift.
