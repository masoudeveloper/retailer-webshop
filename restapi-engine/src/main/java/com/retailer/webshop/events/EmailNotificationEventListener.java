package com.retailer.webshop.events;

import com.retailer.webshop.services.NotificationService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailNotificationEventListener implements ApplicationListener<EmailNotificationEvent> {

  private final NotificationService notificationService;

  @Override
  public void onApplicationEvent(EmailNotificationEvent event) {
    if (event.getSource() != null) {
      Map<String, String> data = (Map<String, String>) event.getSource();
      notificationService.sendEmail(data.get("recipient"), data.get("subject"),
          data.get("content"));
    }
  }
}
