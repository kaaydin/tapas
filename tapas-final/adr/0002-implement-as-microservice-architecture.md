# Implement as Microservice Architecture

Date: 2023-09-28

## Status

Accepted

## Context

In order to fulfill the application's requirements best, we need to evaluate and define a target architecture. TAPAS is an application that allows users to create tasks based on which the system searches for an appropriate executor in the executor pool. 

The (revised) architecture characteristics which were selected are used to decide on a specific architectural style. 


The 7 selected characteristics are: 
* Scalability
* Elasticity
* Modularity
* Interoperability
* Recoverability
* Fault Tolerance
* Evolvability

of which, Scalability, Elasticity, and Modularity are considered the top 3 driving characteristics. 

Based on those characteristics, we select the following architecture styles for further consideration: 
* Microservices
* Event-Driven

## Decision

Both the Microservices, and Event-Driven architecture rank highly when compared to our selected architecture characteristics, mainly on elasticity, evolvability, fault tolerance and scalability.

However, the event-driven adds significant overhead due to asynchronous communication. Also, when compared to the Microservices architecture, the Event-Driven architecture is technically partitioned (vs. domain partitioned). 

Hence, we decided to implement a Microservices architecture based on the above reason.

## Consequences

By using the Microservices architecture, we are able to add changes to our system more flexibly (compared to other architectural styles) as we do not test the entire application (but only the affected architectures). 

To allow for efficient (i.e., low latency) communication amongst the services, we will develop all components individually and create an API layer to handle client requests. 

However, this will lead to higher initial overhead in the implementation of use cases. 
