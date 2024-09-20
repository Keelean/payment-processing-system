package com.keelean.paymentprocessor.steps;

import com.keelean.paymentprocessor.api.model.*;
import com.keelean.paymentprocessor.controller.ProcessPaymentController;
import com.keelean.paymentprocessor.external.AcquiringBankGatewayWebClient;
import com.keelean.paymentprocessor.external.BankTransferGatewayWebClient;
import com.keelean.paymentprocessor.service.TransactionService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import com.keelean.paymentprocessor.cucumber.CucumberSpringConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;


@CucumberContextConfiguration
public class PaymentProcessorSteps extends CucumberSpringConfiguration {

    private MockWebServer mockWebServer;
    private ProcessPaymentController processPaymentController;
    Mono<ResponseEntity<PPPaymentResponse>> responseEntityMono;
    PPPaymentRequest paymentRequest;

    @Before
    public void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString())
                .build();
        TransactionService transactionService = new TransactionService(transactionRepository);
        BankTransferGatewayWebClient bankTransferGatewayWebClient = new BankTransferGatewayWebClient(webClient);
        AcquiringBankGatewayWebClient acquiringBankGatewayWebClient = new AcquiringBankGatewayWebClient(webClient);
        this.processPaymentController = new ProcessPaymentController(acquiringBankGatewayWebClient, bankTransferGatewayWebClient, transactionService);
    }

    @Given("A customer provides his card details to make a purchase")
    public void aCustomerProvidesHisCardDetailsToMakeAPurchase() {
        var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "message":"Payment completed successfully",
                            "success":true
                        }
                        """);
        mockWebServer.enqueue(mockResponse);
        paymentRequest = paymentRequest(PPPaymentRequest.TxnTypeEnum.CARD);
    }

    private PPPaymentRequest paymentRequest(PPPaymentRequest.TxnTypeEnum txnTypeEnum) {
        paymentRequest = new PPPaymentRequest();
        paymentRequest.setTxnAmount(500.00);
        paymentRequest.setTxnType(txnTypeEnum);
        PPPayeeDetails payeeDetails = new PPPayeeDetails();
        PPMerchantDetails merchantDetails = new PPMerchantDetails();
        merchantDetails.setMerchantAccount("8909876543");
        merchantDetails.setMerchantCode("890986");
        payeeDetails.setMerchant(merchantDetails);
        paymentRequest.setPayee(payeeDetails);
        PPPayerDetails payerDetails = getPayerDetails(txnTypeEnum);
        paymentRequest.setPayer(payerDetails);
        return paymentRequest;
    }

    private static @NotNull PPPayerDetails getPayerDetails(PPPaymentRequest.TxnTypeEnum txnTypeEnum) {
        PPPayerDetails payerDetails = new PPPayerDetails();
        PPCardDetails cardDetails = new PPCardDetails();
        PPBankDetails bankDetails = new PPBankDetails();
        if(txnTypeEnum == PPPaymentRequest.TxnTypeEnum.CARD){
            cardDetails.cardName("Kingsley Amaeze");
            cardDetails.setCardNumber("549000001234689456");
            cardDetails.setCvv(688);
            cardDetails.pin(1234);
            cardDetails.setExpiryDate("09/24");
            payerDetails.card(cardDetails);
        }else {
            bankDetails.setAccountNumber("000000000");
            bankDetails.setBankCode("000000");
            payerDetails.setBank(bankDetails);
        }
        return payerDetails;
    }

    @When("He invokes the payment endpoint")
    public void heInvokesThePaymentEndpoint() throws Exception {
        responseEntityMono = processPaymentController.processPayment(Mono.just(paymentRequest), null);

    }

    @Given("A customer provides his bank account details to make a purchase")
    public void aCustomerProvidesHisBankAccountDetailsToMakeAPurchase() {
        var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "message":"Payment completed successfully",
                            "success":true
                        }
                        """);
        mockWebServer.enqueue(mockResponse);
        paymentRequest = paymentRequest(PPPaymentRequest.TxnTypeEnum.BANK_TRANSFER);
    }

    @Then("The payment is successful")
    public void thePaymentIsSuccessful() {
        Mono<PPPaymentResponse> ppPaymentResponseMono = responseEntityMono.map(HttpEntity::getBody);
        StepVerifier.create(ppPaymentResponseMono)
                .expectNextMatches(e-> e.getMessage().equals("Payment completed successfully"))
                .verifyComplete();
    }

    //@Before
    public void prepareData() {
        jdbcTemplate.execute("""
                TRUNCATE TABLE transaction;
                """);
    }

    @After
    public void cleanUp() throws IOException {
        this.mockWebServer.shutdown();
    }
}
