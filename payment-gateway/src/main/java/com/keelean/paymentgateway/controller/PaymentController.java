package com.keelean.paymentgateway.controller;

import com.keelean.paymentgateway.api.PaymentApi;
import com.keelean.paymentgateway.api.model.PGPaymentRequest;
import com.keelean.paymentgateway.api.model.PGPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
public class PaymentController implements PaymentApi {

    @Override
    public Mono<ResponseEntity<PGPaymentResponse>> makePayment(Mono<PGPaymentRequest> paymentRequestMono, ServerWebExchange exchange) throws Exception {
        return PaymentApi.super.makePayment(paymentRequestMono, exchange);
    }
}
