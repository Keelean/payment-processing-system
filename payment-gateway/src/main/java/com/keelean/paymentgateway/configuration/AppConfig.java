package com.keelean.paymentgateway.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.TIME_BASED;

@Configuration
@AllArgsConstructor
public class AppConfig {

    private final PaymentGatewayProperties apiGatewayProperties;

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("i18n/message");

        rs.setCacheSeconds(3600);
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean("messageSourceV2")
    public ResourceBundleMessageSource messageSourceV2() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("i18n/message");

        rs.setCacheSeconds(3600);
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean
    WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(apiGatewayProperties.getExternalPaymentProcessorEndpointUrl())
                .build();
    }
}
