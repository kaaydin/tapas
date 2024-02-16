# Overall Data Ownership and Access

Date: 2023-10-14

## Status

Status depending on:
* [ADR0010](0010-task-data-ownership-and-access.md)
* [ADR0011](0011-executor-data-ownership-and-access.md)

## Context
This ADR provides an overview of our ideas concerning Data Ownership and Access across our TAPAS Implementation. 

For our application, we decided to implement a Joint Ownership - Delegate Technique for Executors and Task List. This gives us the flexibility of having the Executors services connect to the Task List database and perform updates when necessary (specifically for the Executed Task Output). Meanwhile, we implemented a Single Ownership structure for the Executor Pool, Roster, and Task List as each service has its own database.  Below is a figure illustrating our Data Access and Ownership policy. 

![Tapas_Data_Policy](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/main/adr/images/Data%20Owenrship%20%26%20Data%20Access.png "TAPAS Data Ownership and Access Policy")

## Decision

Integration of the Roster Service and Task Execution management requires decisions in our Data Ownership Structure:
* Roster Service has its own database.
* Executor Pool Service has its own database.
* Task List Service has its own database.
* Executors need to have write privileges for patching operations on the Task List Database.

## Consequences

### Positive Consequences:
* Approach allows for the abstraction of data structures from other services, promoting needed modularity and separation of concerns.
* Roster Service can maintain control over its data and operations without interfering with or being affected by other services. This helps ensure data consistency across different services.

### Negative Consequences:
* Higher latency may be experienced due to the presence of multiple databases.

## Disclaimer
Since this ADR is more an overall overview, please refer to the more detailed ADR to see our current implementation.
* [ADR0012](0012-executor-data-ownership-and-access.md)
