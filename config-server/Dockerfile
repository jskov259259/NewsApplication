FROM openjdk:17-jdk-slim
EXPOSE 8888
COPY build/libs/config-server.jar config-server.jar
ENTRYPOINT ["java","-jar","config-server.jar"]