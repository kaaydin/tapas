# Implementing Executors & Assignment Logic within Executor Pool Domain

Date: 2023-10-07

## Status

Superseded by [12. Refactoring of Executors & Roster](0012-refactoring-of-executors-and-roster.md)

## Context

We started the TAPAS project by building an initial version of the Executor Pool. 
Ultimately, the Executor Pool should only be responsible management of executors. 

## Decision

As a first prototype, we chose to house both (1) our Executors and (2) the task assignment logic (referred to as the Roster service) within the Executor Pool domain. We made this choice because it serves two main purposes:

- It simplifies the initial implementation, making it easier to quickly verify the core functionality.
- It reduces architectural complexity, which in turn facilitates easier debugging and maintenance during the initial phase.

This decision, however, comes with certain trade-offs (e.g., significant refactoring). In subsequent iterations, our plan is to separate these components into distinct microservices, aligning with the strategy outlined in ADRs 3 to 7. This approach aims to enhance modularity and better accommodate the evolving needs of the system.

## Consequences

We need to connect the Task List service to the Executor Pool service. When we build the Executors and the Roster service as separate microservices, we need to change the implementation within the Executor Pool and the connections between the Microservices to ensure seamless interaction.  
