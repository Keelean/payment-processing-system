package com.keelean.paymentprocessor.external;

import com.keelean.paymentprocessor.api.model.PPPaymentRequest;
import com.keelean.paymentprocessor.api.model.PPPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class AcquiringBankGatewayWebClient {

    private static final String PAYMENT_API = "/api/v1/payments";

    private final WebClient webClient;

    public AcquiringBankGatewayWebClient(@Qualifier("acquiringBankGatewayWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PPPaymentResponse> processPayment(Mono<PPPaymentRequest> paymentRequestMono){
        return webClient.post()
                .uri(PAYMENT_API)
                .headers(this::headers)
                .body(paymentRequestMono, PPPaymentRequest.class)
                .retrieve()
                .bodyToMono(PPPaymentResponse.class)
                .timeout(Duration.ofSeconds(1), Mono.empty())
                .onErrorResume(WebClientResponseException.ServiceUnavailable.class, exception -> Mono.empty())
                //.retryWhen(Retry.backoff(3, Duration.ofSeconds(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }

    private void headers(HttpHeaders httpHeaders) {
        httpHeaders.add("Content-Type", "application/json");
    }

}
