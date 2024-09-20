package com.keelean.banktransfergateway.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties
public class BankTransferGatewayProperties {

    private String externalEstateEndpointUrl;
    private String externalOnboardingEndpointUrl;
}
