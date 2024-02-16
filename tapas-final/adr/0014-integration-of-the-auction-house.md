# Integration of the Auction House

Date: 2023-11-25

## Status

Accepted

-> Supercedes [5. Dedicated service for Auction Domain](0005-dedicated-service-for-auction-domain.md)

## Context

We are currently implementing the Auction House as part of our Tapas Application that deals primarily with tasks for which no internal executor could be found. 
The Auction House acts as the point of coordination and communication with the other groups. Via the Auction House, a group can either (i) launch an auction 
when no internal executor is found and (ii) bid on an auction from another group only if a suitable executor exists. Ultimately, at the end of the auction, 
the Auction House must notify the winning bidder that it has own & ensure that the task is executed. 

## Decision

As we are working with multiple group, there are parts of the implementation that need to conform to the overall process. Other parts internal-facing requiring
no alignment with other teams. We decided on the following process: 

### No suitable executor is found internally

1. The Roster service makes a request to the auction in case no internal executor is available
2. The Auction House service creates an auction and publishes the auction (via WebSub or MQTT)
3. The Auction House service collects bids placed from other teams on this auction & chooses winning bidder
4. The Auction House service sends the task information to the winning bidder's team task, creating a shadow task
5. The winning bidder's executor successfully completes task & sends updates the original task list

### Bidding on tasks
1. At initialization, we register our executors to the Auction House
2. When an auction is published (via WebSub or MQTT) that meets the requirements of our executors (i.e., Task Type), the Auction House sends a bid
3. If the bid is won, the Auction House of the Publisher sends a request to our internal Task List, creating a shadow task
4. Ultimately, our executor successfully completes the task & sends updates the original task list

Some things to keep in mind with this execution
- Please note, that the shadow task follows the normal task flow as defined originally (i.e., from task list, to roster & then executor)
- The shadow task links to the original task with a speciifed field "originalTaskUri" so that the updates can be successfully completed 

## Consequences
The idea of creating a shadow tasks is a good way to make use of the already existing task flow (i.e., task list, roster, executor) and thus to limit any potential additional requirements in terms of time. 

However, this decision comes with certain drawbacks, such as 
- Increased complexity: Instead of the sending the won task directly to the Auction House (or Executor), using the normal task flows requires use of resources that might not be otherwise needed (e.g., Roster) leading potentially to latency in task execution
- Data Integrity: We need to ensure that the shadow task is synchronized with the original task requires robust update mechanisms to avoid discrepancies (also in the Roster service)
- Communication Overheads: Frequent communication between services, especially in a high-volume environment, might lead to bandwidth and performance issues.

Another consequence is that we register our executors to the Auction when starting the service. This works well in this setup as we don't have many groups. However, as we think about scaling up, we might need to register more executors automatically & dynamically based on the number of requests so that application still works under heavy workload. 
