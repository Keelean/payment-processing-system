package com.keelean.paymentprocessor.controller;

import com.keelean.paymentprocessor.api.ProcessPaymentApi;
import com.keelean.paymentprocessor.api.model.PPBankDetails;
import com.keelean.paymentprocessor.api.model.PPCardDetails;
import com.keelean.paymentprocessor.api.model.PPPaymentRequest;
import com.keelean.paymentprocessor.api.model.PPPaymentResponse;
import com.keelean.paymentprocessor.external.AcquiringBankGatewayWebClient;
import com.keelean.paymentprocessor.external.BankTransferGatewayWebClient;
import com.keelean.paymentprocessor.service.TransactionService;
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
    private final TransactionService transactionService;


    @Override
    public Mono<ResponseEntity<PPPaymentResponse>> processPayment(Mono<PPPaymentRequest> paymentRequestMono, ServerWebExchange exchange) throws Exception {

        return paymentRequestMono.flatMap(request -> {
            if(request.getTxnType() == PPPaymentRequest.TxnTypeEnum.CARD){
                Mono<PPCardDetails> ppCardDetails = Mono.just(extractCardDetailsFromRequest(request));

                return acquiringBankGatewayWebClient.processPayment(ppCardDetails).map(e -> status(HttpStatus.CREATED).body(e));
            }
            Mono<PPBankDetails> ppCardDetails = Mono.just(extractBankDetailsFromRequest(request));
            return bankTransferGatewayWebClient.sendAuthorizationRequest(ppCardDetails).map(e -> status(HttpStatus.CREATED).body(e));
        });

    }

    private PPCardDetails extractCardDetailsFromRequest(PPPaymentRequest request) {
        PPCardDetails cardDetails = new PPCardDetails();
        cardDetails.setExpiryDate(request.getPayer().getCard().getExpiryDate());
        cardDetails.setCardNumber(request.getPayer().getCard().getCardNumber());
        cardDetails.setExpiryDate(request.getPayer().getCard().getExpiryDate());
        cardDetails.setCvv(request.getPayer().getCard().getCvv());
        cardDetails.setPin(request.getPayer().getCard().getPin());
        return cardDetails;
    }

    private PPBankDetails extractBankDetailsFromRequest(PPPaymentRequest request) {
        PPBankDetails bankDetails = new PPBankDetails();
        bankDetails.bankCode(request.getPayer().getBank().getBankCode());
        bankDetails.setAccountNumber(request.getPayer().getBank().getAccountNumber());
        return bankDetails;
    }
}
