package com.keelean.paymentprocessor.external;

import com.keelean.paymentprocessor.api.model.PPCardDetails;
import com.keelean.paymentprocessor.api.model.PPPaymentResponse;
import com.keelean.paymentprocessor.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.keelean.paymentprocessor.util.AppUtil.defaultError;
import static com.keelean.paymentprocessor.util.AppUtil.defaultTimeoutError;

@Component
@Slf4j
public class AcquiringBankGatewayWebClient {

    private static final String PAYMENT_API = "/api/v1/payments";

    private final WebClient webClient;

    public AcquiringBankGatewayWebClient(@Qualifier("acquiringBankGatewayClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PPPaymentResponse> processPayment(Mono<PPCardDetails> cardDetailsMono){
        return webClient.post()
                .uri(PAYMENT_API)
                .headers(AppUtil::headers)
                .body(cardDetailsMono, PPCardDetails.class)
                .retrieve()
                .bodyToMono(PPPaymentResponse.class)
                .log()
                .timeout(Duration.ofSeconds(1), defaultTimeoutError())
                .onErrorResume(WebClientResponseException.ServiceUnavailable.class, exception -> defaultTimeoutError())
                //.retryWhen(Retry.backoff(3, Duration.ofSeconds(100)))
                .onErrorResume(Exception.class, exception -> defaultError());
    }


}
