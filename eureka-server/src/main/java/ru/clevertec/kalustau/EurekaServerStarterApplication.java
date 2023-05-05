package ru.clevertec.kalustau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerStarterApplication.class, args);
    }

}
