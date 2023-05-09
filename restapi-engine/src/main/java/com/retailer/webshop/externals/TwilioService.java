package com.retailer.webshop.externals;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {

  private final boolean isActive;
  private final String accountSid;
  private final String authToken;
  private final String twilioPhoneNumber;

  public TwilioService(TwilioProperties twilioProperties) {
    this.isActive = twilioProperties.isActive();
    this.accountSid = twilioProperties.getAccountSid();
    this.authToken = twilioProperties.getAuthToken();
    this.twilioPhoneNumber = twilioProperties.getPhoneNumber();
  }

  public void sendSms(String mobileNumber, String messageBody) {
    if (isActive) {
      Twilio.init(accountSid, authToken);
      Message.creator(new PhoneNumber(mobileNumber), new PhoneNumber(twilioPhoneNumber),
          messageBody).create();
    }
  }
}