package com.keelean.paymentgateway.cucumber;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.FILTER_TAGS_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
/*@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ng.propertly.estatemgtservice.steps")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")*/
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @user-image-upload")
public class PaymentGatewayTestSuite {
}
