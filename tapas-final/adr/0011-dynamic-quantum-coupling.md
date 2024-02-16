# Dynamic Quantum Coupling

Date: 2023-10-19

## Status

Proposed

## Context

In the TAPAS application, each of our microservices is its own architectural quantum. An architectural quantum is an independently deployable component with high functional cohesion, which includes all the structural elements required for the systems to run properly, including data. 

Thus, we need to evaluate how we can balance the three Coupling Forces as outlined in the lecture: Communication, Consistency and Coordination. The collective evaluation determines the level of dynamic coupling. 

## Decision

### Communication 
In our current architecture, microservices communicate via synchronous HTTP calls, leading to a dynamic quantum entanglement. By only changing the adapters (i.e., advantage of the hexagonal architecture), we can move to async communication. 

Advantages:

- Highly decoupled systems 
- Higher performance and scale 

Disadvantages:
- Increased difficulty in debugging  
- More difficult to implement


### Consistency
In our current architecture, we maintain consistency across our microservices through events (e.g., status updates, task output updates). To maintain eventual consistency, we need synchronize data into a consistent state. 

Advantages:
- More resilient to network failures and partitions
- Can scale out more easily since services can continue to operate independently even if some are slow or failing

Disadvantages:

- Eventual consistency may not be intuitive for developers and could lead to bugs if not handled properly
- Complex to trace and debug due to the asynchronous nature of event propagation

### Coordination
Currently, we have not implemented an orchestrator in our application. Rather, once a task is added to the Task List, the Task List starts the Roster, which in turn accesses the Executor Pool and so on. Hence, we will focus on choreography as our fundamental coordination pattern. 

Advantages: 
- Higher scalability, fault tolerance and responsiveness
- Stronger service decoupling

Disadvantages: 
- More difficult state management 
- Distributed workflow

## Consequences

### Communication
We need to refactor our implementation and change the code in the adapters to enable async communication.

### Consistency
We need to refactor our code to allow updates in the database of the task list based on changes communicated by the executor (i.e., task status and output updates).

### Coordination
No consequence as already coordination is already implemented choreographically.  
