# tapas-executor-mirocard

Micro-service for the Tapas Executor mirocard. 


## HTTP API Overview
Executor integrates implements CoAP for direct communication with the MiroCard system, facilitates real-time data acquisition of environmental parameters, including humidity and temperature and incorporates the Eclipse Californium framework, a Java-based open-source CoAP implementation, to handle CoAP transactions and data processing.

### Sending Task to Executor

A task can be sent to the executor via an `HTTP POST` request to the `/execute` endpoint. The body of the request 
must include the location to the task, which is asked to be executed using the content type `application/start-task+json` 
defined in the context of this project. Please note that before sending out the HTTP POST, you need to create a task first
(via the tapas-tasks microservice).

* `taskLocation`: a string that represent the URI for the task to be executed

Add a new Taks with following HTTP request:
```shell
curl --location 'http://localhost:8081/tasks/' \
--header 'Content-Type: application/task+json' \
--data '{
  "taskName" : "Task2",
  "taskType" : "MIROCARD",
  "inputData" : "humidity"
}'

HTTP/1.1 201 Created
Location: http://tapas-tasks:8081/tasks/f5378f91-0116-4aa2-9524-81e7b9633e99
Content-Length: 0
Date: Sat, 23 Dec 2023 16:41:12 GMT
```



Execute added Task after you set your taskLocation in JSON body received in the headers from the request above:
```shell
curl --location 'http://localhost:8087/execute' \
--header 'Content-Type: application/start-task+json' \
--data '{
  "taskLocation": "http://tapas-tasks:8081/tasks/f5378f91-0116-4aa2-9524-81e7b9633e99"
}'

HTTP/1.1 201 Created
Content-Length: 0
Date: Sat, 23 Dec 2023 15:44:08 GMT
```

If the task is successfully completed, a `200 OK` status code is returned. The task output can 
be checked when sending a GET request to the task location. 

```json
{
  "taskId": "4a20e073-5c26-40ac-ae91-422f78d4e18e",
  "taskName": "Task2",
  "taskType": "MIROCARD",
  "taskStatus": "EXECUTED",
  "inputData": "humidity",
  "outputData": "22.1"
}
```
