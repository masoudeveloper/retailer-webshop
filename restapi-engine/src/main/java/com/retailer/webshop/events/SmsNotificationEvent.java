package com.retailer.webshop.events;

import org.springframework.context.ApplicationEvent;

public class SmsNotificationEvent extends ApplicationEvent {

  public SmsNotificationEvent(Object source) {
    super(source);
  }

}
