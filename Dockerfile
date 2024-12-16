FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ARG SPRING_PROFILE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE:-default}

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]