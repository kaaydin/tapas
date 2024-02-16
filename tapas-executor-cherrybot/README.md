# tapas-executor-cherrybot

Micro-service for the Tapas Executor cherrybot. 


## HTTP API Overview
Cherrybot Executor is an executor designed for controlling the UFACTORY xArm7 Robot (cherrybot) through an HTTP API. 
Its possible to direct control the physical cherrybot located in the laboratory
(see livestream https://interactions.ics.unisg.ch/61-102/cam2/live-stream) but also interact 
with six additional simulated robots (pretendabots) that emulate the cherrybot's functionality.


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
  "taskName" : "TaskCherrybot",
  "taskType" : "CHERRYBOT",
  "inputData" : "{\"action\": \"setTarget\",\"payload\": {\"target\": {\"coordinate\": {\"x\": 300,\"y\": 0,\"z\": 900},\"rotation\": {\"roll\": 180,\"pitch\": 0,\"yaw\": 0}},\"speed\": 350}}"
}'

HTTP/1.1 201 Created
Location: http://tapas-tasks:8081/tasks/6827ca19-19c9-4ade-9070-cf9acece3604
Content-Length: 0
Date: Sat, 23 Dec 2023 16:20:39 GMT
```

```json
{
  "taskId": "6827ca19-19c9-4ade-9070-cf9acece3604",
  "taskName": "TaskCherrybot",
  "taskType": "CHERRYBOT",
  "taskStatus": "OPEN",
  "inputData": "{\"action\": \"setTarget\", \"payload\": {\"target\": {\"coordinate\": {\"x\": 300, \"y\": 0, \"z\": 900}, \"rotation\": {\"roll\": 180, \"pitch\": 0, \"yaw\": 0}}, \"speed\": 350}}"
}
```

Execute Task with `curl` to set the cherrybot to the specified target position:
```shell
curl --location 'http://localhost:8086/execute' \
--header 'Content-Type: application/start-task+json' \
--data '{
  "taskLocation": "http://tapas-tasks:8081/tasks/6827ca19-19c9-4ade-9070-cf9acece3604"
}'

HTTP/1.1 200
Content-Length: 0
Date: Sat, 23 Dec 2023 16:23:45 GMT
```

If the task is successfully completed, a `200 OK` status code is returned. The task output can 
be checked when sending a GET request to the task location or the movement of the physical cherrybot located in our laboratory 
can be checked over the livestream on https://interactions.ics.unisg.ch/61-102/cam2/live-stream. 

```json
{
  "taskId": "6827ca19-19c9-4ade-9070-cf9acece3604",
  "taskName": "TaskCherrybot",
  "taskType": "CHERRYBOT",
  "taskStatus": "EXECUTED",
  "inputData": "{\"action\": \"setTarget\", \"payload\": {\"target\": {\"coordinate\": {\"x\": 300, \"y\": 0, \"z\": 900}, \"rotation\": {\"roll\": 180, \"pitch\": 0, \"yaw\": 0}}, \"speed\": 350}}",
  "outputData": "Response: 200"
}
```
