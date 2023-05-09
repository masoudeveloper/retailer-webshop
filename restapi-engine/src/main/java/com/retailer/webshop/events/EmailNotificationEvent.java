package com.retailer.webshop.events;

import org.springframework.context.ApplicationEvent;

public class EmailNotificationEvent extends ApplicationEvent {

  public EmailNotificationEvent(Object source) {
    super(source);
  }

}
