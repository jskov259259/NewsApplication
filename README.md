Система управления новостями

RESTful web-service, реализующей функционал для работы с системой управления новостями.

Project Tech Stack
Java 17
Gradle 7.6
Git
Spring Boot 3
Spring Data JPA
Spring Security
Spring AOP
Spring Cloud Eureka
Spring Cloud Config
PostgreSQL
Liquibase
Redis
JUnit 5
Mockito
TestContainers
WireMock
Protobuf
Logback
Lombok
Swagger
Docker

Project Requirements
JDK 17+
Gradle 7.6+
Docker Engine 20.10.23+

Run Application
-gradlew build
Run config service and eureka-server:
-java -jar config-server/build/libs/config-server.jar
-java -jar eureka-server/build/libs/eureka-server.jar
Run news and user services
-java -jar news-service/build/libs/news-service.jar
-java -jar user-service/build/libs/user-service.jar

Run application in docker:
docker-compose up

Modules
Config Server:
It's a central place to manage external properties for all application.
Properties are stored in a remote repository on github:
https://github.com/jskov259259/news-application-config
Service starts on port 8888

Eureka Server:
Server holds the information about all client-service applications.
Every Micro service registers into the Eureka server and Eureka server knows all the client applications running on each port and IP address
Service starts on port 8761

Exception Starter:
A module that provides a global exception handler and stores all types of exceptions for the application.

Log Starter:
A module that provides a LogController annotation for request-response logging at the Rest Controller level.
The service also provides logging by levels in all layers of the application

News Service:
A service that implements a news management system.
Available endpoints:

Possible profiles:
