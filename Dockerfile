# Dockerfile for Java 21
FROM bellsoft/liberica-openjdk-alpine:21

COPY build/libs/toastit-0.0.1.jar app.jar

CMD ["java", "-jar", "app.jar"]