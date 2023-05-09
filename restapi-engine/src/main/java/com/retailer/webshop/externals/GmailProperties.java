package com.retailer.webshop.externals;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "gmail")
public class GmailProperties {

  private boolean active;
  private String mailAddress;
  private String password;
  private String smtpHost;
  private String smtpPort;
}
