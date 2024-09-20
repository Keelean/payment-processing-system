package com.keelean.paymentprocessor.controller;

import com.keelean.paymentprocessor.api.ProcessPaymentApi;
import com.keelean.paymentprocessor.api.model.PPPaymentRequest;
import com.keelean.paymentprocessor.api.model.PPPaymentResponse;
import com.keelean.paymentprocessor.external.AcquiringBankGatewayWebClient;
import com.keelean.paymentprocessor.external.BankTransferGatewayWebClient;
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
public class ProcessPaymentController implements ProcessPaymentApi {

    private final AcquiringBankGatewayWebClient acquiringBankGatewayWebClient;
    private final BankTransferGatewayWebClient bankTransferGatewayWebClient;

    @Override
    public Mono<ResponseEntity<PPPaymentResponse>> processPayment(Mono<PPPaymentRequest> paymentRequestMono, ServerWebExchange exchange) throws Exception {
        PPPaymentRequest paymentRequest = paymentRequestMono.block();
        if(paymentRequest.getTxnType() == PPPaymentRequest.TxnTypeEnum.CARD){
            return acquiringBankGatewayWebClient.processPayment(paymentRequestMono).map(e -> status(HttpStatus.CREATED).body(e));
        }else {
            return bankTransferGatewayWebClient.sendAuthorizationRequest(paymentRequestMono).map(e -> status(HttpStatus.CREATED).body(e));
        }
    }
}
