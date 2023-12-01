# Dockerfile/Docker-Compose file based on an initial version authored by Alexander Lontke (ASSE, Fall Semester 2021)

FROM maven as build

COPY . /app

RUN mvn -f app/pom.xml --batch-mode --update-snapshots verify

FROM openjdk

COPY --from=build /app/target/tapas-auction-house-0.0.1.jar ./tapas-auction-house-0.0.1.jar

CMD java -jar tapas-auction-house-0.0.1.jar
