package com.keelean.banktransfergateway.cucumber;


import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = SpringTestConfiguration.class)
public class CucumberSpringConfiguration {

    @Autowired
    protected WebTestClient webTestClient;


}
