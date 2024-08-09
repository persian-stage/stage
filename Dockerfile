FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y postgresql-client

WORKDIR /app

COPY target/stage-0.0.1-SNAPSHOT.jar /app/stage.jar
COPY .env /app/.env

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "stage.jar"]