# tapas-executor-pool

Micro-service for the Executor Pool implemented following Hexagonal Architecture. It contains functionality to add and remove internal executors
as well as finding and assigning an internal executor for a specific task. It contains some simplified logic for the finding and assigning part.

Based on examples from book "Get Your Hands Dirty on Clean Architecture" by Tom Hombergs

Technologies: Java, Spring Boot, Maven

**Note:** this repository contains an [EditorConfig](https://editorconfig.org/) file (`.editorconfig`)
with default editor settings. EditorConfig is supported out-of-the-box by the IntelliJ IDE. To help maintain
consistent code styles, we recommend to reuse this editor configuration file in all your services.

## HTTP API Overview
The code we provide includes a minimalistic uniform HTTP API for (i) adding a new executor, (ii) retrieving
an executor by id, (iii) retrieving an executor by type.

### Adding a new executor

A new executor is added via an `HTTP POST` request to the `/executors/` endpoint. The body of the request
must include a representation of the executor to be added using the content type `application/executor+json`
defined in the context of this project. 

* `executorName`: a string that represents the name of the executor to be added
* `executorType`: a string that represents the type of the executor to be added
* `executorBaseUri`: a string that represents the baseUri of the executor to be added

A sample HTTP request with `curl`:
```shell
curl -i --location --request POST 'http://localhost:8082/executors/' \
--header 'Content-Type: application/executor+json' \
--data-raw '{
  "executorName" : "Executor1",
  "executorType" : "CALCULATING",
  "executorBaseUri" : "http://tapas-executor-calculator:8084"
}

HTTP/1.1 201
Location: http://localhost:8082/executors/f8c7a7b4-d839-42b3-a6b6-387f2c974c93
Content-Length: 0
Date: Sat, 07 Oct 2023 19:41:12 GMT

```

If the executor is added successfully, a `201 Created` status code is returned together with a
`Location` header field that points to the URI of the newly created executor.

### Retrieving an executor by ID

The representation of an executor is retrieved via an `HTTP GET` request to the URI of /executors/{executorId}.

A sample HTTP request with `curl`:
```shell
curl -i --location --request GET 'http://localhost:8082/executors/91a7a032-0cb6-45d5-9c4d-7bcee6780985'

HTTP/1.1 200
Content-Type: application/executor+json
Content-Length: 170
Date: Sun, 17 Oct 2021 21:07:04 GMT

{
  "executorName" : "Executor1",
  "executorType" : "CALCULATING",
  "executorBaseUri" : "http://tapas-executor-calculator:8084"
}

### Retrieving executors by type

The representation of an executor is retrieved via an `HTTP GET` request to the URI of executors/type/{executorType}.

A sample HTTP request with `curl`:
```shell
curl -i --location --request GET 'http://localhost:8082/executors/type/CALCULATING'

HTTP/1.1 200
Content-Type: application/executor+json
Content-Length: 170
Date: Sun, 17 Oct 2021 21:07:04 GMT

[
  {
    "executorName" : "Executor1",
    "executorType" : "CALCULATING",
    "executorBaseUri" : "http://tapas-executor-calculator:8084"
  }
]
```
