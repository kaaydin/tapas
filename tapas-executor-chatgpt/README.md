# tapas-executor-chatgpt

Micro-service for the Tapas Executor ChatGPT. 

This executor takes in a question about coding and returns a "sassy" and "funny" answer. 
The input is retrieved from the task list (via a GET request based on the task location). 
After execution, the output is then passed onto the task list (another microservice) to update the task output and status. 

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
  "taskName" : "Task_GPT",
  "taskType" : "PROMPTING",
  "inputData" : "What is COBOL?"
}'

HTTP/1.1 201 Created
Location: http://tapas-tasks:8081/tasks/327a09ef-eba3-4983-891a-2fa0c75c2f80
Content-Length: 0
Date: Sat, 23 Dec 2023 16:29:22 GMT
```

```json
{
  "taskId": "327a09ef-eba3-4983-891a-2fa0c75c2f80",
  "taskName": "Task_GPT",
  "taskType": "PROMPTING",
  "taskStatus": "OPEN",
  "inputData": "What is COBOL?"
}
```


The OpenAI Key is just simply hardcoded, therefore replace it with your own key in [ChatGPTExecutor.java line 22](./tapas-executor-chatgpt/src/main/java/ch/unisg/tapaschatgpt/ChatGPTExecutor.java#L22).

Execute added Task after you set your taskLocation in JSON body received in the headers from the request above:
```shell
curl --location 'http://localhost:8085/execute' \
--header 'Content-Type: application/start-task+json' \
--data '{
  "taskLocation": "http://tapas-tasks:8081/tasks/327a09ef-eba3-4983-891a-2fa0c75c2f80"
}'

HTTP/1.1 200 Created
Content-Length: 0
Date: Sat, 23 Dec 2023 16:43:02 GMT
```

If the task is successfully completed, a `200 OK` status code is returned. The task output can 
be checked when sending a GET request to the task location. 

```json
{
  "taskId": "327a09ef-eba3-4983-891a-2fa0c75c2f80",
  "taskName": "Task_GPT",
  "taskType": "PROMPTING",
  "taskStatus": "EXECUTED",
  "inputData": "What is COBOL?",
  "outputData": "Oh, COBOL? That's a language used by ancient coding fossils who refuse to let go of the past and embrace the wonders of the modern world."
}
```






