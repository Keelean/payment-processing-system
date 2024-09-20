package com.keelean.paymentgateway.steps;

import com.keelean.paymentgateway.api.model.*;
import com.keelean.paymentgateway.controller.PaymentController;
import com.keelean.paymentgateway.external.PaymentProcessorWebClient;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import com.keelean.paymentgateway.cucumber.CucumberSpringConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

@CucumberContextConfiguration
public class PaymentGatewaySteps extends CucumberSpringConfiguration {

    private MockWebServer mockWebServer;
    private PaymentController paymentController;
    Mono<ResponseEntity<PGPaymentResponse>> responseEntityMono;
    PGPaymentRequest paymentRequest;

    @Before
    public void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString())
                .build();
        PaymentProcessorWebClient paymentProcessorWebClient = new PaymentProcessorWebClient(webClient);
        this.paymentController = new PaymentController(paymentProcessorWebClient);
    }

    @Given("A customer provides his card details including the amount his paying for the goods or services and his PIN")
    public void aCustomerProvidesHisCardDetailsIncludingTheAmountHisPayingForTheGoodsOrServicesAndHisPIN() {
        var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        "message":"Payment completed successfully",
                        "success":true
                        """);
        mockWebServer.enqueue(mockResponse);
        paymentRequest = new PGPaymentRequest();
        paymentRequest.setTxnAmount(500.00);
        paymentRequest.setTxnType(PGPaymentRequest.TxnTypeEnum.CARD);
        PGPayeeDetails payeeDetails = new PGPayeeDetails();
        PGMerchantDetails merchantDetails = new PGMerchantDetails();
        merchantDetails.setMerchantAccount("8909876543");
        merchantDetails.setMerchantCode("890986");
        payeeDetails.setMerchant(merchantDetails);
        paymentRequest.setPayee(payeeDetails);
        PGPayerDetails payerDetails = new PGPayerDetails();
        PGCardDetails cardDetails = new PGCardDetails();
        cardDetails.cardName("Kingsley Amaeze");
        cardDetails.setCardNumber("549000001234689456");
        cardDetails.setCvv(688);
        cardDetails.pin(1234);
        cardDetails.setExpiryDate("09/24");
        payerDetails.card(cardDetails);
        paymentRequest.setPayer(payerDetails);
    }

    @When("He invokes the payment endpoint")
    public void heInvokesThePaymentEndpoint() throws Exception {
        responseEntityMono = paymentController.makePayment(Mono.just(paymentRequest), null);
    }

    @Then("The payment is successful")
    public void thePaymentIsSuccessful() {
        StepVerifier.create(responseEntityMono.map(HttpEntity::getBody))
                .expectNextMatches(PGPaymentResponse::getSuccess);
    }

    @After
    public void cleanUp() throws IOException {
        this.mockWebServer.shutdown();
    }
}
