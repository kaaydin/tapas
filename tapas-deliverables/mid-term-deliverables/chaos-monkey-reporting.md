# Tapas Chaos Engineering Tests

To fully validate and understand the functionality and limitations of our Tapas application, we completed the Chaos Engineering test using Chaos Monkey on our application. Three of the services (Roster, Executor Pool, and Calculator Executor) we have developed were tested under different conditions to simulate potential real-world issues and uncover vulnerabilities across the whole application. 

For all the chaos testing, the test request was `Add Task`. This was purposefully chosen to control for more variables during testing and isolate issues appropriately in the application. Below is a template of the request used. Depending on the test, the request may have been only sent once or multiple times to test how the application acts over time as more requests are inputted. In the case of multiple requests, `taskName` and `inputData` parameters were changed: 

```
curl -i --location --request POST 'http://tapas-tasks:8081/tasks/' \
--header 'Content-Type: application/task+json' \
--data-raw '{
  "taskName" : "task1",
  "taskType" : "computation",
  "inputData" : "1+1"
}'
```

The image below illustrates the architecture and data flow of our TAPAS implementation. 

![Tapas_Architecture](https://github.com/unisg-scs-asse/tapas-fs23-group5/blob/tapas-at-runtime/tapas-deliverables/mid-term-deliverables/images/TAPAS%20Architecture.png "Tapas Application Architecture")

## Roster:

The roster is a central service in the application. It functions by matching incoming tasks to executors. When a new task is added, the roster communicates with the executor pool to match the task type to the executor type. Once matched, the executor pool informs the roster of the presence of an executor. The roster then sends the tasks to this executor (a separate service), where the task is completed.

Due to our implementation, the roster stands at the center of the application and interacts with all the other services. As a result of this centrality, both latency and exception assaults were undertaken to understand its vulnerabilities. 

### Latency Assault:

A latency assault involves adding random latency (delay) to a request as it passes through the designated service. In this case, the roster. 

In order to accomplish this test, once the application was initiated, the Chaos Monkey assault was initiated using the POST request below:

```
curl -X POST http://localhost:8083/chaosmonkey/assaults \
-H 'Content-Type: application/json' \
-d \
'
{
	"latencyRangeStart": 2000,
	"latencyRangeEnd": 5000,
	"latencyActive": true,
	"exceptionsActive": false,
	"killApplicationActive": false
}'
```

Once this was started, different requests were sent to the TAPAS application to Add a Task. 

The ensuing response from the application was stored. Then the application was restarted without turning on any Chaos Moneky assaults to have a benchmark of how the application would have behaved without the latency assault.

From our testing, we found that the most direct result is a longer time to complete the task. This is expected as a latency assault functions by adding a delay to the amount of time required to complete the request. In the case of the roster, the amount of time required to match the task to an executor and assign the task to the executor are being delayed, therefore a delay is occurring as two points in the application.

Although these tests are isolated, these results can be extrapolated to a larger scale, where the TAPAS application may be working with more requests. In this case, such delays would likely result in delays in a deteriorating user experience. In our case, we only did one request as a time to isolate the effect of a latency attack. But in the case of multiple requests, this delay would add up over time. This would cause delays across the whole application; executor assignment, executor addition, and potential resource constraints as a bottleneck begins to occur at the roster. This could break the whole application. 

As a result, the roster is a delicate service and crucial to the strong functioning of the application. Attention to its fault-tolerance should be taken particularly into account given our findings. 

### Exception Assault:

An exception assault involves throwing random runtime exceptions in the service. A runtime exception is a programming error or unexpected condition that occurs at runtime and is not required to be caught at compile time. These kinds of exceptions can halt the execution of the whole service or at least for that particular request. 

In order to accomplish this test, once the application was initiated, the Chaos Monkey assault was initiated using the POST request below:

```
curl -X POST http://localhost:8083/chaosmonkey/assaults \
-H 'Content-Type: application/json' \
-d \
'
{
	"latencyActive": false,
	"exceptionsActive": true,
	"killApplicationActive": false
}'
```

Once this was started, different requests were sent to the TAPAS application to Add a Task. 

In our current implementation, this kind of error leads to the whole request failing to be completed. Due to runtime exceptions multiple arise. Because the roster is the receiver of the tasks, a runtime exception error at the roster results in a task being added to the task list, but nothing happens with this task as no executor assignment or matching takes place. A complete failure in the executor assignment could also take place.  Additionally, if the Roster service is not functioning properly, the initiation of execution will also fail.

The culmination of these errors will result in the whole TAPAS application failing as tasks back up due to runtime exceptions. For this reason, a similar discovery is made as with latency assault. Due to the central nature of the Roster, it must be made very fault-tolerant in order to avoid the whole application failing.

## Executor Pool:

The executor pool functions as a repository of available executors (keeping quantity, type, and current status of executor information). It communicates with the roster and its main function is to inform the roster of executor availability. For our purposes, the exception assault was chosen to be tested as it is believed this would bring about the most interesting results. Latency results are likely to only delay the system and the application results, meanwhile, random runtime exceptions can lead to more interesting behaviors.

### Exception Assault:

In the context of the executor pool, an exception assault adds random runtime exceptions when the service is called by the roster to check and communicate the status of a particular type of executor. 

In order to accomplish this test, once the application was initiated, the Chaos Monkey assault was initiated using the POST request below:

```
curl -X POST http://localhost:8082/chaosmonkey/assaults \
-H 'Content-Type: application/json' \
-d \
'
{
	"latencyActive": false,
	"exceptionsActive": true,
	"killApplicationActive": false
}'
```

Once this was started, different requests were sent to the TAPAS application to Add a Task. 

The most direct consequence of an exception assault is the failure to retrieve an executor. When the roster sends a request to the Executor Pool to retrieve a list of suitable executors based on task type, the process will fail when there is a runtime exception. Therefore the roster cannot obtain the appropriate executors for the task. As an extension of this, task execution is not possible as the task would remain unassigned. This could result in a backlog of unassigned tasks. Although this situation could eventually shut down the application, because it is isolated to the Executor Pool, the whole system is more robust against these errors. These kinds of issues could be addressed by adding logic to the executor pool service to try multiple times to add or retrieve an executor in case the first time fails. 

## Executor - Calculator:

The calculator executor is one of the two types of executors we currently have in the system. This executor takes in two number inputs from the user and a mathematical operator and applies it to the inputted numbers. It is a simple functionality, that serves very well to focus on only testing the communication across the application before adding more complexity to each executor where other complications may arise. The exception assault was tested on the calculator executor because, like the executor pool, the errors from added latency seem to be quite straightforward. Meanwhile, random runtime exceptions may result in more revealing behavior about the application.

### Exception Assault:

In the context of the calculator executor, an exception assault adds random runtime exceptions when the service is called by the roasted to complete a given task. 

In order to accomplish this test, once the application was initiated, the Chaos Monkey assault was initiated using the POST request below:

```
curl -X POST http://localhost:8084/chaosmonkey/assaults \
-H 'Content-Type: application/json' \
-d \
'
{
	"latencyActive": false,
	"exceptionsActive": true,
	"killApplicationActive": false
}'
```

Once this was started, different requests were sent to the TAPAS application to Add a Task. 

The direct result of a runtime exception error in the calculator executor is a failure of execution. The calculator executor is responsible for executing calculation tasks. The result of an exception in this service would mean that the task is not processed. This leads to an immediate failure in executing said task. Additionally, the calculator executor communicates back to the task list. If the runtime error occurs, then potentially no update is sent to the task list. This would result in a task not being completed without the user being informed about the situation. On a larger scale, if this issue were to occur. The application would continue running although as time passes, more tasks would not be completed and their status would not be updated in the task list. This would result in user confusion. 

These tests reveal that the application is quite robust against failures in the executors and will likely continue functioning in the case of such chaos. Yet, its functionality would be diminished as tasks would not be completed and users would be unaware of the situation. As a result of this, additional fault-tolerant methods should be added to the service to still inform the task list even when an executor fails. 

### Circuit Breaker Test:

To illustrate this point, a circuit breaker was added to Calculator Executor and the exception assaults tests were repeated. The Circuit Break functions by monitoring the service. In our implementation, if a failure happens, the circuit breaker will open. This means that the request will not be processed instead a fallback method will be triggered. In our case, the fallback method is a print statement, that states: 'Task execution failed at Executor'. This simple implementation is meant to address the issue of communication between executors and the task list. With this simple print statement, we can now readily show when there is an error through the application logs. This is a simple implementation to address the main drawback of communication. More elaborate implementations could be made through a half-open circuit for example, whereby after a predefined recovery period, Hystrix allows some requests to pass through and tests if the executor is functioning properly. This more dynamic approach would solve the communication issue whilst additionally allowing the system to recover if the issue is temporary. 
