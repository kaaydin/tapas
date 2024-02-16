# tapas-executor-masonsmanifesto

Micro-service for the Tapas Executor masonsmanifesto. 

This executor is the gateway to the profound wisdom of our colleague's Masonic quotes API. \
Check out the documentation: https://manifest-of-mason.vercel.app/api-reference 


## HTTP API Overview
The code we provide includes a minimalistic uniform HTTP API for sending a task (to be executed) to the executor. 
The quotes itself is provided by our colleague's own API service. 

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
  "taskName" : "Task_Manifest",
  "taskType" : "MANIFESTO",
}'

HTTP/1.1 201 Created
Location: http://tapas-tasks:8081/tasks/ba93b310-6a0b-4a29-81bd-934c84122656
Content-Length: 0
Date: Sat, 23 Dec 2023 15:50:40 GMT
```

Execute added Task after you set your taskLocation in JSON body received in the headers from the request above:
```shell
curl --location 'http://localhost:8088/execute' \
--header 'Content-Type: application/start-task+json' \
--data '{
  "taskLocation": "http://tapas-tasks:8081/tasks/ba93b310-6a0b-4a29-81bd-934c84122656"
}'

HTTP/1.1 201 Created
Location: http://tapas-tasks:8081/tasks/48583c85-8e96-46f6-8379-4e53d823278b
Content-Length: 0
Date: Sat, 23 Dec 2023 16:11:35 GMT
```

If the task is successfully completed, a `200 OK` status code is returned. The task output can 
be checked when sending a GET request to the task location. 

```json
{
  "taskId": "7f5e07dc-a6ac-456d-ba58-e46e024ca821",
  "taskName": "Task_Manifesto",
  "taskType": "MANIFESTO",
  "taskStatus": "EXECUTED",
  "outputData": "What is an ADR?"
}
