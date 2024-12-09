FROM openjdk:17-alpine

WORKDIR /app

COPY target/*.jar app.jar

ARG PROFILE

ENV SPRING_PROFILES_ACTIVE=${PROFILE:-default}

CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]
