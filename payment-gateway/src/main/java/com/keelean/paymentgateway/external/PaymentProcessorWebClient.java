package com.keelean.paymentgateway.external;

import com.keelean.paymentgateway.api.model.PGPaymentRequest;
import com.keelean.paymentgateway.api.model.PGPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class PaymentProcessorWebClient {

    private static final String PAYMENT_API = "/api/v1/payments";

    private final WebClient webClient;

    public PaymentProcessorWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PGPaymentResponse> makePayment(Mono<PGPaymentRequest> paymentRequestMono) {
        return webClient.post()
                .uri(PAYMENT_API)
                .headers(this::headers)
                .body(paymentRequestMono, PGPaymentRequest.class)
                .retrieve()
                .bodyToMono(PGPaymentResponse.class)
                .timeout(Duration.ofSeconds(1), Mono.empty())
                .onErrorResume(WebClientResponseException.ServiceUnavailable.class, exception -> Mono.empty())
                //.retryWhen(Retry.backoff(3, Duration.ofSeconds(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }

    private void headers(HttpHeaders httpHeaders) {
        httpHeaders.add("Content-Type", "application/json");
    }

}
