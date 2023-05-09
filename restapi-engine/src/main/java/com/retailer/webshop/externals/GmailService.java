package com.retailer.webshop.externals;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailService {

  private final boolean isActive;
  private final String gmailEmailAddress;
  private final String gmailPassword;
  private final String smtpHost;
  private final String smtpPort;

  public GmailService(GmailProperties gmailProperties) {
    this.isActive = gmailProperties.isActive();
    this.gmailEmailAddress = gmailProperties.getMailAddress();
    this.gmailPassword = gmailProperties.getPassword();
    this.smtpHost = gmailProperties.getSmtpHost();
    this.smtpPort = gmailProperties.getSmtpPort();
  }

  public void sendEmail(String recipientEmailAddress, String emailSubject, String emailContent) {
    if (isActive) {
      Properties properties = System.getProperties();
      properties.put("mail.smtp.host", smtpHost);
      properties.put("mail.smtp.port", smtpPort);
      properties.put("mail.smtp.ssl.enable", "true");
      properties.put("mail.smtp.auth", "true");

      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(gmailEmailAddress, gmailPassword);
        }
      });

      try {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(gmailEmailAddress));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmailAddress));
        message.setSubject(emailSubject);
        message.setText(emailContent);
        Transport.send(message);
      } catch (MessagingException mex) {
        mex.printStackTrace();
      }
    }
  }
}
