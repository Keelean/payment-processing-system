package com.keelean.banktransfergateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankTransferGatewayApplication {

	public static void main(String[] args) {

		//SpringApplication.run(ApiGatewayApplication.class, args);
		SpringApplication app = new SpringApplication(BankTransferGatewayApplication.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}

}
