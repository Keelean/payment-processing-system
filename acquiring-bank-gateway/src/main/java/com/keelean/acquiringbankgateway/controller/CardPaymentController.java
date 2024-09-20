package com.keelean.acquiringbankgateway.controller;

import com.keelean.acquiringbankgateway.api.ProcessCardPaymentApi;
import com.keelean.acquiringbankgateway.api.model.PPCardDetails;
import com.keelean.acquiringbankgateway.api.model.PPPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
public class CardPaymentController implements ProcessCardPaymentApi {

    @Override
    public Mono<ResponseEntity<PPPaymentResponse>> processCard(Mono<PPCardDetails> ppCardDetails, ServerWebExchange exchange) throws Exception {
        return ProcessCardPaymentApi.super.processCard(ppCardDetails, exchange);
    }
}
