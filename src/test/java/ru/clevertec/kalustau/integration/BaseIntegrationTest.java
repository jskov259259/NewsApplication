package ru.clevertec.kalustau.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Sql({"classpath:db/schema.sql", "classpath:db/data.sql"})
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public abstract class BaseIntegrationTest {

    private static final String DOCKER_IMAGE_NAME = "postgres:11.1";

    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME);

    @BeforeAll
    static void init() {
        container.start();
    }

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}