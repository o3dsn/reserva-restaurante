FROM openjdk:17-alpine

WORKDIR /app

COPY target/*.jar app.jar

ARG SPRING_PROFILE

CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "app.jar"]
