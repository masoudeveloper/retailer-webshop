package com.retailer.webshop.events;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void publishCheckoutEvent(NotificationType notificationType, String recipient,
      String content, String subject) {

    Map<String, String> data = new HashMap<>();
    data.put("recipient", recipient);
    data.put("subject", subject);
    data.put("content", content);

    switch (notificationType) {
      case SMS -> applicationEventPublisher.publishEvent(new SmsNotificationEvent(data));
      case EMAIL -> applicationEventPublisher.publishEvent(new EmailNotificationEvent(data));
    }
  }
}
