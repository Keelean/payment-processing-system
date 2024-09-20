package com.keelean.paymentprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentProcessorApplication {

	public static void main(String[] args) {

		//SpringApplication.run(ApiGatewayApplication.class, args);
		SpringApplication app = new SpringApplication(PaymentProcessorApplication.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}

}
