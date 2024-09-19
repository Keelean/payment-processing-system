package com.keelean.paymentprocessor;

import org.springframework.boot.SpringApplication;

public class TestPaymentProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.from(PaymentProcessorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
