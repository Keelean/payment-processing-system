package com.keelean.paymentprocessor.cucumber;


import com.keelean.paymentprocessor.repository.TransactionRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = SpringTestConfiguration.class)
@Testcontainers
public class CucumberSpringConfiguration {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected TransactionRepository transactionRepository;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    static {
        postgres.start();
    }


}
