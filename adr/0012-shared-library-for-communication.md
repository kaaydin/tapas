# Shared Library for Communication

Date: 2023-10-19

## Status

Proposed

## Context

Currently, we use a lot of standard HTTP client / server code to enable communication between microservices. This leads to a lot of code duplication. 

## Decision

We will create a separate helper class library that will help us to create standardized HTTP requests more easily. 

Advantages: 
- Code bundling leads to less duplication
- Shared code is compile-based, reducing runtime errors

Disadvantages: 
- Increased complications & efforts for version management
- Dependencies can be difficult to manage

## Consequences
We need to refactor our current code and create a separate module. Also, we have to ensure that the dependencies and versions are well managed so that we will not run into mistakes while compiling. 

