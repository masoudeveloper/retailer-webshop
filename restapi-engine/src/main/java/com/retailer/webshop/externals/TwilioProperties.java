package com.retailer.webshop.externals;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {

  private boolean active;
  private String accountSid;
  private String authToken;
  private String phoneNumber;
}
