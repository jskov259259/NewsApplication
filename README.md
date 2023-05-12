# News management system
Application for managing news and comments

## Idea
Develop a RESTful web service that implements functionality for working with a news management system.
Add the ability to authenticate and differentiate by roles.

## Project Requirements
* JDK 17+
* Gradle 7.6+
* Docker Engine 20.10.23+

## Project Tech Stack
* Java 17
* Gradle 7.6
* Git
* Spring Boot 3
* Spring Data JPA
* Spring MVC
* Spring Security
* Spring AOP
* Spring Cloud Eureka
* Spring Cloud Config
* PostgreSQL
* Liquibase
* Redis
* JUnit 5
* Mockito
* TestContainers
* WireMock
* Protobuf
* Logback
* Lombok
* Swagger
* Docker

## How to start
#### Clone the project from the repository
git clone https://github.com/jskov259259/NewsApplication.git
#### Build Application
gradlew build

#### Run config service and eureka-server:
java -jar config-server/build/libs/config-server.jar  
java -jar eureka-server/build/libs/eureka-server.jar

#### Run news and user services
-java -jar news-service/build/libs/news-service.jar  
-java -jar user-service/build/libs/user-service.jar

### Run application in docker:
docker-compose up

## Modules
### Config Server:
It's a central place to manage external properties for all application.
Properties are stored in a remote repository on github:
https://github.com/jskov259259/news-application-config
Service starts on port 8888

### Eureka Server:
Server holds the information about all client-service applications.
Every Micro service registers into the Eureka server and Eureka server knows all the client applications running on each port and IP address
Service starts on port 8761

### Exception Starter:
A module that provides a global exception handler and stores all types of exceptions for the application.

### Log Starter:
A module that provides a LogController annotation for request-response logging at the Rest Controller level.
The service also provides logging by levels in all layers of the application

### News Service:
A service that implements a news management system.  
**Available endpoints**:  
*GET /api/v1/news* - Find all news  
*GET /api/v1/news/{id}* - Find news by id  
*POST /api/v1/news* - Create news  
*PUT /api/v1/news/{id}* - Update news
*DELETE /api/v1/news/{id}* - Delete news by id 

*GET /api/v1/comments* - Find all comments  
*GET /api/v1/comments/{id}* - Find comment by id  
*GET /api/v1/news/{newsId}/comments* - Find all comments by specifying news id  
*POST /api/v1/news/{newsId}/comments* - Create comment for a specific news  
*PUT /api/v1/comments/{id}* - Update comment by id  
*DELETE /api/v1/comments/{id}* - Delete comment by id  

**Possible profiles**:  
*custom-cache*: Run application with custom LRU or LFU implementations  
*redis*: Run application with a cache implementation in the form of Redis DB

### User Service:
Service for user authorization and authentication. Issues a token based on the user's role.
Possible roles: admin, journalist, subscriber.

**Available endpoints**:  
POST api/auth/login - user authorization and authentication  
POST api/auth/register - Register a new user  
GET api/auth/user/{token} - get user by token  
POST api/auth/token - refresh user token  
POST api/auth/refresh - refresh user refresh token  

#### Swagger OpenAPI documentation:
http://localhost:8080/swagger-ui.html

