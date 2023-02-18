FROM openjdk:17

WORKDIR /app

COPY build/libs/file-service.jar /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "file-service.jar"]