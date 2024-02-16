# tapas-f23-group-5

## Quick-Start Guide
1. Deploy all micro-services locally or on the VM (via Docker)
2. Send a POST request to the executor-pool micro-service to register executors (more detailed information on how to do that in the README)
    1. For Calculator: Use “CALCULATING” as “executorType” and “http://tapas-executor-calculator:8084/” as “executorBaseUri” 
    2. For ChatGPT: Use “PROMPTING” as “executorType” and “http://tapas-executor-chatgpt:8085/” as “executorBaseUri”
    3. For Cherrybot: Use “CHERRYBOT” as “executorType” and “http://tapas-executor-cherrybot:8086/” as “executorBaseUri” 
    4. For Mirocard: Use “MIROCARD” as “executorType” and “http://tapas-executor-chatgpt:8087/” as “executorBaseUri
    5. For MasonsManifesto: Use "MASON" as executorType and "http://tapas-executor-masonsmanifesto:8088/” as "executorBaseUri"
3. Send a POST request to the tapas-tasks micro-service with your input (more detailed information on how to do that in the README)
    1. For the Calculator: You can set simple mathematical operations as task input (e.g., “5+5”)
    2. For the ChatGPT: You can set simple coding questions as task input (e.g., “What is ChaosMonkey?”)
    4. For the Cherrybot: You can set an action and payload based on the Thing Description as task input (e.g., “"{\"action\": \"initialize\",\"payload\": {}}”)
    6. For the Mirocard: You can set the sensor data as task input (e.g., “humidity”)
    7. For the Masons Manifesto: No inputData needed
    8. For an external executor: You need to set an executorType and task input that aligns with the external executor. Make sure that both auction houses communicate over the same protocol (either WebSub or MQTT).

Once the POST request has been sent to tapas-tasks micro-service, the requests gets sent to the the roster. The roster automatically (1) assigns the task to an existing executor and (2) sends the task location to the respective executor. The executor retrieves the task input, executes the task and sends the updates to the task list. In case the roster cannot find an executor that is able to execute the task, the task is sent from the roster to the auction house where the task is auctioned off to other auction houses that can bid on the auction. The task is sent to the external auction house with the winning bid, where the task is assigned and executed by the external executor. The external executor then updates the task in the task list.

The task output can be retrieved by sending a GET request to the task location (i.e., the task URI).

## Project Structure
This project is structured as follows:
* [app](app): a very simple web service developed using Spring Boot, based on the tutorial at https://spring.io/guides/gs/spring-boot/
    * PORT 8080
* [tapas-tasks](tapas-tasks): standalone project for the Tapas-Tasks micro-service (Spring Boot project)
    * [tapas-tasks/README.md](tapas-tasks/README.md): README file for the Tapas Tasks service with more details.
    * [tapas-tasks/src](tapas-tasks/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-tasks/pom.xml](tapas-tasks/pom.xml): Maven pom-file
    * PORT 8081
* [tapas-executor-pool](tapas-executor-pool): standalone project for the Tapas-Executor-Pool micro-service (Spring Boot project)
    * [tapas-executor-pool/README.md](tapas-executor-pool/README.md): README file for the Tapas-Executor-Pool service with more details.
    * [tapas-executor-pool/src](tapas-executor-pool/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-executor-pool/pom.xml](tapas-executor-pool/pom.xml): Maven pom-file
    * PORT 8082
* [tapas-roster](tapas-roster): standalone project for the tapas-roster micro-service (Spring Boot project)
    * [tapas-roster/README.md](tapas-roster/README.md): README file for the tapas-roster service with more details.
    * [tapas-roster/src](tapas-roster/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-roster/pom.xml](tapas-roster/pom.xml): Maven pom-file
    * PORT 8083
* [tapas-executor-calculator](tapas-executor-calculator): standalone project for the tapas-executor-calculator micro-service (Spring Boot project)
    * [tapas-executor-calculator/README.md](tapas-executor-calculator/README.md): README file for the tapas-executor-calculator service with more details.
    * [tapas-executor-calculator/src](tapas-executor-calculator/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-executor-calculator/pom.xml](tapas-executor-calculator/pom.xml): Maven pom-file
    * PORT 8084
* [tapas-executor-chatgpt](tapas-executor-chatgpt): standalone project for the tapas-executor-chatgpt micro-service (Spring Boot project) 
    * [tapas-executor-chatgpt/README.md](tapas-executor-chatgpt/README.md): README file for the tapas-executor-chatgpt service with more details.
    * [tapas-executor-chatgpt/src](tapas-executor-chatgpt/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-executor-chatgpt/pom.xml](tapas-executor-chatgpt/pom.xml): Maven pom-file
    * PORT 8085
* [tapas-executor-cherrybot](tapas-executor-cherrybot): standalone project for the tapas-executor-cherrybot micro-service (Spring Boot project) 
    * [tapas-executor-cherrybot/README.md](tapas-executor-cherrybot/README.md): README file for the tapas-executor-cherrybot service with more details.
    * [tapas-executor-cherrybot/src](tapas-executor-cherrybot/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-executor-cherrybot/pom.xml](tapas-executor-cherrybot/pom.xml): Maven pom-file
    * PORT 8086
* [tapas-executor-mirocard](tapas-executor-mirocard): standalone project for the tapas-executor-mirocard micro-service (Spring Boot project) 
    * [tapas-executor-mirocard/README.md](tapas-executor-mirocard/README.md): README file for the tapas-executor-mirocard service with more details.
    * [tapas-executor-mirocard/src](tapas-executor-mirocard/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-executor-mirocard/pom.xml](tapas-executor-mirocard/pom.xml): Maven pom-file
    * PORT 8087
* [tapas-executor-masonsmanifesto](tapas-executor-masonsmanifesto): standalone project for the tapas-executor-masonsmanifesto micro-service (Spring Boot project) 
    * [tapas-executor-masonsmanifesto/README.md](tapas-executor-masonsmanifesto/README.md): README file for the tapas-executor-masonsmanifesto service with more details.
    * [tapas-executor-masonsmanifesto/src](tapas-executor-masonsmanifesto/src): source code of the project (following the Hexagonal Architecture)
    * [tapas-executor-masonsmanifesto/pom.xml](tapas-executor-masonsmanifesto/pom.xml): Maven pom-file
    * PORT 8088



* [docker-compose.yml](docker-compose.yml): Docker Compose configuration file for all services
* [docker-compose-local.yml](docker-compose-local.yml): Docker Compose configuration file to run all services on local Docker
* [.github/workflows/build-and-deploy.yml](.github/workflows/build-and-deploy.yml): GitHub actions script (CI/CD workflow)

## How to Add a New Service with Spring Boot

### Create a new Spring Boot project

* Recommended: use [Spring Initialzr](https://start.spring.io/) (Maven, Spring Boot 2.7.14, Jar, Java 17, dependencies as needed)
* Set the Spring application properties for your service (e.g., port of the web server) in `src/resources/application.properties`

### Update the Docker Compose file
Your TAPAS application is a multi-container Docker application ran with [Docker Compose](https://docs.docker.com/compose/).
To add your newly created service to the Docker Compose configuration file, you need to create a new service
definition in [docker-compose.yml](docker-compose.yml):
* copy and edit the `tapas-tasks` service definition from [lines 44-57](docker-compose.yml)
* change `command` (see [line 46](docker-compose.yml)
  to use the name of the JAR file generated by Maven for your service
    * note: if you change the version of your service, you need to update this line to reflect the change
* update the Traefik label names to reflect the name of your new service (see [line 52](docker-compose.yml))
    * e.g., change `traefik.http.routers.tapas-tasks.rule` to `traefik.http.routers.<new-service-name>.rule`
* update the Traefik `rule` (see [line 53](docker-compose.yml)) with the name of your new service: ``Host(`<new-service-name>.${PUB_IP}.asse.scs.unisg.ch`)``
* update the Traefik `port` (see [line 54](docker-compose.yml)) with the port configured for your new service

### Update the local Docker Compose file
In the [local docker compose file](docker-compose-local.yml) you need to copy and adjust lines 18-28.

### Update the GitHub Actions Workflow
This project uses GitHub Actions to build and deploy your TAPAS application whenever a new commit is
pushed on the `main` branch. You can add your new service to the GitHub Actions workflow defined in
[.github/workflows/build-and-deploy.yml](.github/workflows/build-and-deploy.yml):
* copy and edit the definition for `tapas-tasks` from [line 27-29](.github/workflows/build-and-deploy.yml)
* update the `mvn` command used to build your service to point to the `pom.xml` file of your new service (see [line 28](.github/workflows/build-and-deploy.yml))
* update the `cp` command to point to the JAR file of your new service directive (see [line 29](.github/workflows/build-and-deploy.yml))
    * note you will need to update the complete file path (folder structure and JAR name)

### How to Run the TAPAS Tasks Service Locally
You can run your micro-services on your local machine just like a regular Maven project:
* Run from IntelliJ:
    * Reload *pom.xml* if necessary
    * Run the micro-service's main class from IntelliJ for all required projects
    * Adapt the run configuration for the main file to include the VM argument:
      ```-Dspring.profiles.active=local``` to let Spring Boot use the application-local.properties file (if available).
* Use Maven to run from the command line:
```shell
mvn spring-boot:run -D"Spring-boot.run.profiles=local"
```
* Run with local Docker:
```shell
docker compose -f docker-compose-local.yml up --build
```

## How to Deploy on your VM
1. Start your Ubuntu VM on Switch.
    * VM shuts down automatically at 2 AM
    * Group admins can do this via https://engines.switch.ch/horizon
2. Push new code to the *main* branch
    * Check the status of the workflow on the *Actions* page of the GitHub project
    * We recommend to test your project locally before pushing the code to GitHub.
3. Open in your browser `https://app.${PUB_IP}.asse.scs.unisg.ch`

For the server IP address (see below), you should use dashes instead of dots, e.g.: `127.0.0.1` becomes `127-0-0-1`.

## VM Configurations

Specs (we can upgrade if needed):
* 2 CPUs
* 2 GB RAM
* 40 GB HD
* Ubuntu 22.04

| Name               | Server IP     |
|--------------------|---------------|
| SCS-ASSE-VM-Group1 | 86.119.35.40  |
| SCS-ASSE-VM-Group2 | 86.119.35.213 |
| SCS-ASSE-VM-Group3 | 86.119.34.242 |
| SCS-ASSE-VM-Group4 | 86.119.35.199 |
| SCS-ASSE-VM-Group5 | 86.119.35.72  |
| SCS-ASSE-VM-Group6 | 86.119.47.205 |
| SCS-ASSE-VM-Group7 | 86.119.45.187 |
| SCS-ASSE-VM-Group8 | 86.119.44.195 |

## Architecture Decision Records
We recommend to use [adr-tools](https://github.com/npryce/adr-tools) to manage your ADRs here in
this GitHub project in a dedicated folder. The tool works best on a Mac OS or Linux machine.
