FROM maven:3.9.1-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache postgresql-client

COPY --from=build /app/target/stage-0.0.1-SNAPSHOT.jar /app/stage.jar

COPY .env /app/.env

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "stage.jar"]
