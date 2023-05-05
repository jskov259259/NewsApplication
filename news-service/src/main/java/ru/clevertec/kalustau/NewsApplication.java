package ru.clevertec.kalustau;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "News API", version = "1.0", description = "News application Information"))
public class NewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExceptionStarterApplication.class, args);
    }

}
