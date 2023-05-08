package ru.clevertec.kalustau.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.clevertec.kalustau.NewsApplication;

@Sql({"classpath:db/schema.sql", "classpath:db/data.sql"})
@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = NewsApplication.class)
public abstract class BaseIntegrationTest {

    private static final String DOCKER_IMAGE_NAME = "postgres:11.1";

    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    @BeforeAll
    static void init() {
        container.start();
    }

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}