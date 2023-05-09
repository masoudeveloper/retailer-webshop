package com.retailer.webshop.services.impl;

import com.retailer.webshop.externals.GmailService;
import com.retailer.webshop.externals.TwilioService;
import com.retailer.webshop.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final GmailService gmailService;
  private final TwilioService twilioService;

  @Override
  public void sendEmail(String emailAddress, String subject, String content) {
    gmailService.sendEmail(emailAddress, subject, content);
  }

  @Override
  public void sendSms(String mobile, String content) {
    twilioService.sendSms(mobile, content);
  }
}
