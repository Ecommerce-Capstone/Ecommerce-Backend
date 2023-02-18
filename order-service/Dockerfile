FROM openjdk:17

WORKDIR /app

COPY build/libs/order-service.jar /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "order-service.jar"]