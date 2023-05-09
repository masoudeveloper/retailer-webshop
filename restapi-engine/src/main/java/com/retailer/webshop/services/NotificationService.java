package com.retailer.webshop.services;

public interface NotificationService {

  void sendEmail(String emailAddress, String subject, String content);

  void sendSms(String mobile, String content);
}
