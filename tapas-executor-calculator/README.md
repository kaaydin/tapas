# tapas-executor-calculator

Micro-service for the Tapas Executor Calculator. 

Following executor takes in two number inputs from the user and a mathematical operator and applies it to the inputted numbers. The input is retrieved 
from the task list (via a GET request based on the task location). After execution, the output is then passed onto the 
task list (another microservice) to update the task output and status. 

Technologies: Java, Spring Boot, Maven

## HTTP API Overview
The code we provide includes a minimalistic uniform HTTP API for sending a task (to be executed) to the executor. 

### Sending Task to Executor

A task can be sent to the executor via an `HTTP POST` request to the `/execute` endpoint. The body of the request 
must include the location to the task, which is asked to be executed using the content type `application/start-task+json` 
defined in the context of this project. Please note that before sending out the HTTP POST, you need to create a task first
(via the tapas-tasks microservice).

* `taskLocation`: a string that represent the URI for the task to be executed

Add a new Taks with the following HTTP request:
```shell
curl --location 'http://localhost:8081/tasks/' \
--header 'Content-Type: application/task+json' \
--data '{
  "taskName" : "TaskCalculator",
  "taskType" : "CALCULATOR",
  "inputData" : "4+16"
}'

HTTP/1.1 201 Created
Location: http://tapas-tasks:8081/tasks/7ec97950-ba20-4a6e-bbd0-1faa7a6e3b05
Content-Length: 0
Date: Sat, 23 Dec 2023 16:45:58 GMT
```

```json
{
  "taskId": "7ec97950-ba20-4a6e-bbd0-1faa7a6e3b05",
  "taskName": "TaskCalculator",
  "taskType": "CALCULATOR",
  "taskStatus": "OPEN",
  "inputData": "4+16"
}
```


Execute added Task after you set your taskLocation in JSON body received in the headers from the request above:
```shell
curl --location 'http://localhost:8084/execute' \
--header 'Content-Type: application/start-task+json' \
--data '{
  "taskLocation": "http://tapas-tasks:8081/tasks/7ec97950-ba20-4a6e-bbd0-1faa7a6e3b05"
}'

HTTP/1.1 200 Created
Content-Length: 0
Date: Sat, 23 Dec 2023 16:49:30 GMT
```

If the task is successfully completed, a `200 OK` status code is returned. The task output can 
be checked when sending a GET request to the task location. 

```json
{
  "taskId": "7ec97950-ba20-4a6e-bbd0-1faa7a6e3b05",
  "taskName": "TaskCalculator",
  "taskType": "CALCULATOR",
  "taskStatus": "EXECUTED",
  "inputData": "4+16",
  "outputData": "20"
}
```

## Hystrix Circuit Breaker
To activate the Hystrix Implementation the `Calculator Executor` class needs to be changed. To isolate the testing, the circuit implementation was commented out. To activate this implementation, the `Calculator Executor` class with Hystrix would have to be uncommented. Replace the current `Calculator Executor` class with the `Calculator Executor` class that currently exists under the code comment `/* Hystrix Circuit Breaker Implementation`.
