package com.keelean.paymentgateway;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;


@ExtendWith({MockitoExtension.class})
class PaymentGatewayApplicationTests {

    Logger log = LoggerFactory.getLogger(PaymentGatewayApplicationTests.class);


    private List<String> values;

    @BeforeEach
    void setUp() {
        values = new ArrayList<>();
        values.add("Michael");
        values.add("John");
        values.add("Edwin");
    }

    @Mock
    ApplicationContext applicationContext;

}
