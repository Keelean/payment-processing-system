package com.keelean.banktransfergateway.configuration;

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

    private final BankTransferGatewayProperties apiGatewayProperties;

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

    @Bean("estateServiceWebClient")
    WebClient estateWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(apiGatewayProperties.getExternalEstateEndpointUrl())
                .build();
    }

    @Bean("authServiceWebClient")
    WebClient authServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(apiGatewayProperties.getExternalOnboardingEndpointUrl())
                .build();
    }

    @Bean
    ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }

    @Bean
    CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(10)
                .slidingWindowType(TIME_BASED)
                .slidingWindowSize(5)
                //.recordException(e -> INTERNAL_SERVER_ERROR.equals(getResponse().getStatus()))
                .recordExceptions(IOException.class, TimeoutException.class)
                //.ignoreException(Exception.class)
                .build();
    }

    @Bean
    CircuitBreakerRegistry circuitBreakerRegistry(){
        return CircuitBreakerRegistry.of(circuitBreakerConfig());
    }

    @Bean
    CircuitBreaker circuitBreakerAuthServerBackend(){
        return circuitBreakerRegistry().circuitBreaker("autherServerBackend");
    }

    @Bean
    CircuitBreaker circuitBreakerEstateServiceBackend(){
        return circuitBreakerRegistry().circuitBreaker("estateServiceBackend", circuitBreakerConfig());
    }

}
