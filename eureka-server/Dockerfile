FROM openjdk:17-jdk-slim
EXPOSE 8761
COPY build/libs/eureka-server.jar eureka-server.jar
ENTRYPOINT ["java","-jar","eureka-server.jar"]