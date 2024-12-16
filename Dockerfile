FROM openjdk:17-alpine

WORKDIR /app

COPY target/*.jar app.jar

ARG SPRING_PROFILE

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE:-default}

CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]
