FROM openjdk:17

WORKDIR /app

COPY build/libs/user-service.jar /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-service.jar"]