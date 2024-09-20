package com.keelean.paymentprocessor.service;

import com.keelean.paymentprocessor.api.model.PPPaymentRequest;
import com.keelean.paymentprocessor.constants.TxnStatus;
import com.keelean.paymentprocessor.entity.Transaction;
import com.keelean.paymentprocessor.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public Mono<Transaction> create(PPPaymentRequest paymentRequest) {
        return Mono.fromCallable(() -> repository.save(Transaction.builder().amount(BigDecimal.valueOf(paymentRequest.getTxnAmount()))
                .status(TxnStatus.ACCEPTED)
                .build()));
    }
}
