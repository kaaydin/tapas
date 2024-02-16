# CHANGE LOG

## EXECUTORS
- Implemented 3 New Executors
  - Added **Robot (CherryBot)** Executor
  - Added **MiroCards** Executor
  - Added **Masons Manifesto** Executor
- The *Robot* and *Mirocard* executors were implemented based on hypermedia API to allow for decoupling
- Improved and extended current and new executors
  - Implemented functionality to update the shadow task and the initial task when an external task is executed
  - Implemented asynchronous task execution
  - Improved test cases for unit testing of the executors
  - Added and improved error handling for communication errors

## AUCTION HOUSE
- Implemented the Auction House
- Introduced and implemented WebSub and MQTT
- Added semantic hypermedia crawler for auction feed discovery

## EXECUTOR POOL
- Fixed issue with retrieving executors by type where only part of the executors were retrieved from cache instead of full list from database

## ROSTER
- Implemented logic to launch an auction in the auction house if no capable executor was found
- Fixed issue with retrieving all task assignments where only part of the assignments were retrieved from cache instead of full list from database
- Fixed issue with assignment where assignment without a matching executor was created

## GENERAL
- Code Maintenance: Continuously cleaned up and refactored our code

## CURRENT LIMITATIONS & POTENTIAL IMPROVEMENTS
- Need for shared code library for executors and communication (was not implemented due to time limitations)
- Implementation of more asynchronous communication; so far only task execution is asynchronous
- Lack of ingestion controls and bandwidth management
- Limited monitoring and logging capabilities
- Develop a front-end or integrate SWAGGER UI
