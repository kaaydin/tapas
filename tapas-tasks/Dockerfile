# Dockerfile/Docker-Compose file based on an initial version authored by Alexander Lontke (ASSE, Fall Semester 2021)

FROM maven as build

COPY . /app

RUN mvn -f app/pom.xml --batch-mode --update-snapshots verify -DskipTests

FROM openjdk

COPY --from=build /app/target/tapas-tasks-0.0.1.jar ./tapas-tasks-0.0.1.jar

CMD java -jar tapas-tasks-0.0.1.jar
