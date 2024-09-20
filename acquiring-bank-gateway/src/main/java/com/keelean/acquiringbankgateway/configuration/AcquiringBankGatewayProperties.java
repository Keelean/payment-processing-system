package com.keelean.acquiringbankgateway.configuration;

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
public class AcquiringBankGatewayProperties {

    private String externalEstateEndpointUrl;
    private String externalOnboardingEndpointUrl;
}
