package com.keelean.paymentgateway.controller;

import com.keelean.paymentgateway.api.PaymentApi;
import com.keelean.paymentgateway.api.model.PGPaymentRequest;
import com.keelean.paymentgateway.api.model.PGPaymentResponse;
import com.keelean.paymentgateway.external.PaymentProcessorWebClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.status;

@Slf4j
@AllArgsConstructor
@RestController
public class PaymentController implements PaymentApi {

    private final PaymentProcessorWebClient paymentProcessorWebClient;

    @Override
    public Mono<ResponseEntity<PGPaymentResponse>> makePayment(Mono<PGPaymentRequest> paymentRequestMono, ServerWebExchange exchange) throws Exception {
        return paymentProcessorWebClient.makePayment(paymentRequestMono).map(e -> status(HttpStatus.CREATED).body(e));
    }
}
