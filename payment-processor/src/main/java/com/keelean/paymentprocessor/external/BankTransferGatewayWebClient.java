package com.keelean.paymentprocessor.external;

import com.keelean.paymentprocessor.api.model.PPBankDetails;
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

@Component()
@Slf4j
public class BankTransferGatewayWebClient {

    private static final String PAYMENT_REQUEST_AUTHORIZATION_API = "/api/v1/requestAuthorization";

    private final WebClient webClient;

    public BankTransferGatewayWebClient(@Qualifier("bankTransferGatewayClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PPPaymentResponse> sendAuthorizationRequest(Mono<PPBankDetails> bankDetailsMono){
        return webClient.post()
                .uri(PAYMENT_REQUEST_AUTHORIZATION_API)
                .headers(AppUtil::headers)
                .body(bankDetailsMono, PPBankDetails.class)
                .retrieve()
                .bodyToMono(PPPaymentResponse.class)
                .log()
                .timeout(Duration.ofSeconds(1), defaultTimeoutError())
                .onErrorResume(WebClientResponseException.ServiceUnavailable.class, exception -> defaultTimeoutError())
                //.retryWhen(Retry.backoff(3, Duration.ofSeconds(100)))
                .onErrorResume(Exception.class, exception -> defaultError());
    }


}
