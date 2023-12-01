# tapas-executor-calculator

Micro-service for the Tapas Executor Calculator. 

The calculator executor is one of the two types of executors we currently have in the system. This executor takes in 
two number inputs from the user and a mathematical operator and applies it to the inputted numbers. The input is retrieved 
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

A sample HTTP request with `curl`:

```shell
curl --location 'http://localhost:8084/execute' \
--header 'Content-Type: application/start-task+json' \
--data '{
  "taskLocation": "http://tapas-tasks:8081/tasks/2d2978af-8a20-4e8f-b0bb-d2480b2581bc"
}'

HTTP/1.1 200
Content-Length: 0
Date: Sat, 07 Oct 2023 19:41:12 GMT

```

If the task is successfully completed, a `200 OK` status code is returned. The task output can 
be checked when sending a GET request to the task location. 