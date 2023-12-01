# Shared Library for Executors

Date: 2023-10-19

## Status

Proposed

## Context

Our executors contain a similar code base to replicate the functionality of an executors, for instance domain logic or communication to and from other microservices. 

## Decision

We will build a seperate executor library that implements the common functionality across executors (i.e., eveything that is not executor-specific).

Advantages: 
- Change in common functionality of executors implemented at a single place 
- Reduction in code duplication
- Shared code is compile-based, reducing runtime errors

Disadvantages: 
- Increased complications & efforts for version management
- Dependencies can be difficult to manage

## Consequences
We need to refractor our current code and create a separate module. Also, we have to ensure that the dependencies and versions are well managed so that we will not run into mistakes while compiling. 

