# tapas-roster

Micro-service for the Roster implemented following Hexagonal Architecture. It contains logic to assign a task given to the roster to an executor capable of executing the task. These assignments are persisted, therefore it also contains the functionality to retrieve all assignments created.

Technologies: Java, Spring Boot, Maven

## HTTP API Overview
The code we provide includes a minimalistic uniform HTTP API for (i) creating an assignment, (ii) retrieving
all assignments

### Create an assignment

An assignment is created via an `HTTP POST` request to the `/roster/newtask/` endpoint. The body of the request
must include a representation of a task to be assigned using the content type `application/roster+task+json`
defined in the context of this project.

* `taskLocation`: a string that represents the URI where the task is located
* `taskType`: a string that represents the type of the task
* `taskNameList`: a string that represents the name of the tasklist

A sample HTTP request with `curl`:
```shell
curl -i --location --request POST 'http://localhost:8083/roster/newtask/' \
--header 'Content-Type: application/roster+task+json' \
--data-raw '{
  "taskLocation" : "http://tapas-tasks:8081/tasks/${taskId}",
  "taskType" : "CALCULATING",
  "taskNameList" : "whatever"
}

Response:
HTTP/1.1 200
{
    "taskAssignmentId": "f72b7f4c-b1b1-4ac0-a265-0696477f24c4",
    "executorBaseUri": {
        "baseUri": "http://tapas-executor-calculator:8084/"
    },
    "taskLocation": "http://tapas-tasks:8081/tasks/52d3a107-5b38-4b93-996a-408decd990e7"
}

```

If the assignment is created successfully, the request returns the assignment in the body.

### Retrieving all assignments

The representation of an executor is retrieved via an `HTTP GET` request to the URI of /roster.

A sample HTTP request with `curl`:
```shell
curl -i --location --request GET 'http://localhost:8083/roster'

HTTP/1.1 200

Response:
[
    {
        "taskAssignmentId": "46edc763-02c0-4213-b7aa-17feac783bef",
        "executorBaseUri": {
            "baseUri": "http://tapas-executor-calculator:8084/"
        },
        "taskLocation": "http://tapas-tasks:8081/tasks/52d3a107-5b38-4b93-996a-408decd990e7"
    }
]

```
