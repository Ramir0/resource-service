FROM openjdk:17.0.1-jdk-slim-buster

WORKDIR /app
COPY bootstrap/target/bootstrap-1.0.0.jar resource-service.jar
EXPOSE 8081

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "resource-service.jar"]
