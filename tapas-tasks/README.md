# tapas-tasks

Micro-service for Managing Tasks in a Task List implemented following Hexagonal Architecture.

Based on examples from book "Get Your Hands Dirty on Clean Architecture" by Tom Hombergs

Technologies: Java, Spring Boot, Maven

**Note:** this repository contains an [EditorConfig](https://editorconfig.org/) file (`.editorconfig`)
with default editor settings. EditorConfig is supported out-of-the-box by the IntelliJ IDE. To help maintain
consistent code styles, we recommend to reuse this editor configuration file in all your services.

## HTTP API Overview
The code we provide includes a minimalistic uniform HTTP API for (i) creating a new task, (ii) retrieving
a representation of the current state of a task, and (iii) patching the representation of a task, which
is mapped to an integration event.

The representations exchanged with the API use two media types:
* a JSON-based format for task with the media type `application/task+json`; this media type is defined
  in the context of our project, but could be [registered with IANA](https://www.iana.org/assignments/media-types)
  to promote interoperability (see
  [TaskJsonRepresentation](src/main/java/ch/unisg/tapastasks/tasks/adapter/in/formats/TaskJsonRepresentation.java)
  for more details)
* the [JSON Patch](http://jsonpatch.com/) format with the registered media type `application/json-patch+json`, which is also a
  JSON-based format (see sample HTTP requests below).

For further developing and working with your HTTP API, we recommend to use [Postman](https://www.postman.com/).

### Creating a new task

A new task is created via an `HTTP POST` request to the `/tasks/` endpoint. The body of the request
must include a representation of the task to be created using the content type `application/task+json`
defined in the context of this project. A valid representation must include at least two required fields
(see [TaskJsonRepresentation](src/main/java/ch/unisg/tapastasks/tasks/adapter/in/formats/TaskJsonRepresentation.java)
for more details):
* `taskName`: a string that represents the name of the task to be created
* `taskType`: a string that represents the type of the task to be created

A sample HTTP request with `curl`:
```shell
curl -i --location --request POST 'https://tapas-tasks.86-119-34-23.asse.scs.unisg.ch/tasks/' \
--header 'Content-Type: application/task+json' \
--data-raw '{
  "taskName" : "task1",
  "taskType" : "computation",
  "inputData" : "1+1"
}'

HTTP/1.1 201
Location: https://tapas-tasks.86-119-34-23.asse.scs.unisg.ch/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354
Content-Length: 0
Date: Sun, 17 Oct 2021 21:03:34 GMT

```

If the task is created successfully, a `201 Created` status code is returned together with a
`Location` header field that points to the URI of the newly created task.

### Retrieving a task

The representation of a task is retrieved via an `HTTP GET` request to the URI of task.

A sample HTTP request with `curl`:
```shell
curl -i --location --request GET 'http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354'

HTTP/1.1 200
Content-Type: application/task+json
Content-Length: 170
Date: Sun, 17 Oct 2021 21:07:04 GMT

{
  "taskId":"cef2fa9d-367b-4e7f-bf06-3b1fea35f354",
  "taskName":"task1",
  "taskType":"computation",
  "taskStatus":"OPEN",
  "inputData":"1+1"
}
```

### Patching a task

REST emphasizes the generality of interfaces to promote uniform interaction. For instance, we can use
the `HTTP PATCH` method to implement fine-grained updates to the representational state of a task, which
may translate to various integration events. However, to conform to the uniform interface
constraint in REST, any such updates have to rely on standard knowledge — and thus to hide away the
implementation details of our service.

In addition to the `application/task+json` media type we defined for our uniform HTTP API, a standard
representation format we can use to specify fine-grained updates to the representation of tasks
is [JSON Patch](http://jsonpatch.com/). In what follow, we provide a few examples of `HTTP PATCH` requests.
For further details on the JSON Patch format, see also [RFC 6902](https://datatracker.ietf.org/doc/html/rfc6902)).

#### Changing the status of a task to EXECUTED

Sample HTTP request that changes the status of the task to `EXECUTED` and adds an output result:

```shell
curl -i --location --request PATCH 'http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354' \
--header 'Content-Type: application/json-patch+json' \
--data-raw '[ {"op" : "replace", "path": "/taskStatus", "value" : "EXECUTED" },
  {"op" : "add", "path": "/outputData", "value" : "2" } ]'

HTTP/1.1 200
Location: http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354
Content-Length: 0
Date: Sun, 17 Oct 2021 21:32:25 GMT

```

Internally, this request is mapped to a
[TaskExecutedEvent](src/main/java/ch/unisg/tapastasks/tasks/application/port/in/TaskExecutedEvent.java).
The HTTP response returns a `200 OK` status code together with location of the resource.

## Working with MongoDB
The provided TAPAS Tasks service is connected to a MongoDB as a repository for persisting data.

Here are some pointers to start integrating the MongoDB with the other microservices:
* [application.properties](src/main/resources/application.properties) defines the
    * URI of the DB server that Spring will connect to (`mongodb`service running in Docker container). Username and password for the server can be found in [docker-compose.yml](../docker-compose.yml).
    * Name of the database for the microservice (`tapas-tasks`)
* [docker-compose.yml](../docker-compose.yml) defines
    * in lines 59-67: the configuration of the mongodb service based on the mongodb container including the root username and password (once deployed this cannot be changed anymore!)
    * in lines 69-87: the configuration of a web application called `mongo-express` to manage the MongoDB server. The web app can be reached via the URI: [http://dbadmin.${PUB_IP}.asse.scs.unisg.ch]([http://dbadmin.${PUB_IP}.asse.scs.unisg.ch]). Login credentials for  mongo-express can be found in lines 89 and 90.
    * in lines 89-90: the volume to be used by the mongodb service for writing and storing data (do not forget!).
* The [pom.xml](./pom.xml) needs to have `spring-boot-starter-data-mongodb` and `spring-data-mongodb` as new dependencies.
* The [TapasTasksApplication.java](src/main/java/ch/unisg/tapastasks/TapasTasksApplication.java) specifies in line 9 the location of the MongoRepository classes for the microservice.
* The [persistence.mongodb](src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb) package has the relevant classes to work with MongoDB:
    * The [MongoTaskDocument.java](src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb/MongoTaskDocument.java) class defines the attributes of a Document for storing a task in the collection `tasks`.
    * The [TaskRepository.java](src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb/TaskRepository.java) class specifies the MongoRepository.
    * The [TaskPersistenceAdapter.java](src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb/TaskPersistenceAdapter.java) implements the two ports to add a new task ([AddTaskPort](src/main/java/ch/unisg/tapastasks/tasks/application/port/out/AddTaskPort.java)) and retrieve a task ([LoadTaskPort](src/main/java/ch/unisg/tapastasks/tasks/application/port/out/LoadTaskPort.java)). These ports are used in the classes [AddNewTaskToTaskListService.java](src/main/java/ch/unisg/tapastasks/tasks/application/service/AddNewTaskToTaskListService.java) and [RetrieveTaskFromTaskListService.java](src/main/java/ch/unisg/tapastasks/tasks/application/service/RetrieveTaskFromTaskListService.java).

#### General hints:
* To not overload the VMs we recommend to use only one MongoDB server that all microservices connect to. Per microservice you could use one database or one collection (discuss in your ADRs!). To use more than one MongoDB server you have to extend the [docker-compose.yml](../docker-compose.yml) file by basically replicating lines 59-90 and changing the names of the services and volumes to be unique (ask your tutors!).
* For running everything locally you can use the [docker-compose-local.yml](../docker-compose-local.yml) file or you have to install the MongoDB server locally on your computers and change the `spring.data.mongodb.uri` String in [application-local.properties](./src/main/resources/application-local.properties). MongoExpress can be reached via http://localhost:8089.

### Integration and System Testing:
* Be aware that when using [docker-compose-local.yml](../docker-compose-local.yml) or the deplyoment to the SwitchVM, the provided tests are not automatically executed during the Maven build process due to runtime dependencies on the MongoDB container.
* For integration testing with a live MongoDB system from your IDE we recommend to use the [docker-compose-local-mongo.yml](../docker-compose-local-mongo.yml) file which only starts up a MongoDB container on your machine that can be reached via localhost:27017. You can then run the integration test file with VM parameter ```-Dspring.profiles.active=local-mongo``` which activates the profile defined in [application-local-mongo.properties](./src/main/resources/application-local-mongo.properties).

### Chaos Tests
* As introduced in the lecture, we are using Chaos Monkey to test the resilience of our application.
* The application*-.properties files contain the configurations of Chaos Monkey that you can adjust to activate different types of assaults as documented, e.g., here https://www.baeldung.com/spring-boot-chaos-monkey.
* Be aware that if you want to run Chaos Monkey using the [docker-compose-local-mongo.yml](../docker-compose-local-mongo.yml) file which just spins up the database containers, you need to activate the "chaos-monkey" Spring profile when starting the application from IntelliJ or the terminal, e.g., by providing ```-ea -Dspring.profiles.active=local-mongo,chaos-monkey``` as VM parameters to start the main class with.
* You can also activate the different types of assaults at runtime via the Chaos Monkey REST API as described, e.g., here: https://softwarehut.com/blog/tech/chaos-monkey or https://codecentric.github.io/chaos-monkey-spring-boot/latest/
* An example request would look as follows:

```shell
curl --location --request POST 'http://127.0.0.1:8081/actuator/chaosmonkey/assaults' \
--header 'Content-Type: application/json' \
--data-raw '{
    "killApplicationActive": true,
    "runtimeAssaultCronExpression": "*/1 * * * * ?",
    "latencyActive": false,
    "exceptionsActive": false,
    "memoryActive": true
}'
```
### Performance Tests with JMeter
* We are using Apache JMeter to perform some load and performance tests for our applications.
* You can download the Java-based application here: https://jmeter.apache.org/download_jmeter.cgi and either use it via its GUI or CLI.
* Note that if you are using JMeter later on in the course to perform some load tests, you may run into limitations regarding the request rates of e.g., the MongoDB, MQTT or WebSub.
