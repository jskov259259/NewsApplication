FROM openjdk:17-jdk-slim
EXPOSE 8081
WORKDIR /app
ARG JAR_FILE=/build/libs/user-service.jar
COPY ${JAR_FILE} /app/user-service.jar
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
