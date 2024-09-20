package com.keelean.paymentprocessor.util;

import com.keelean.paymentprocessor.api.model.PPPaymentResponse;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

public class AppUtil {

    public static void headers(HttpHeaders httpHeaders) {
        httpHeaders.add("Content-Type", "application/json");
    }

    public static Mono<PPPaymentResponse> defaultTimeoutError(){
        PPPaymentResponse paymentResponse = new PPPaymentResponse();
        paymentResponse.setMessage("Service is not available at the moment. Please try again later");
        paymentResponse.setSuccess(false);
        return Mono.just(paymentResponse);
    }

    public static Mono<PPPaymentResponse> defaultError(){
        PPPaymentResponse paymentResponse = new PPPaymentResponse();
        paymentResponse.setMessage("System error");
        paymentResponse.setSuccess(false);
        return Mono.just(paymentResponse);
    }
}
