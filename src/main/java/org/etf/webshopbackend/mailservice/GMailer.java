package org.etf.webshopbackend.mailservice;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static jakarta.mail.Message.RecipientType.TO;
import static org.etf.webshopbackend.mailservice.GmailCredentals.getCredentials;

@Slf4j
@Service
public class GMailer {

  @Value("${app.name}")
  private String appName;
  @Value("${app.email}")
  private String myEmail;

  private final Gmail service;

  public GMailer() throws GeneralSecurityException, IOException {
    NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
        .setApplicationName(appName)
        .build();
  }

  public void sendMail(String toEmail, String subject, String message) throws Exception {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    MimeMessage email = new MimeMessage(session);
    email.setFrom(new InternetAddress(myEmail));
    email.addRecipient(TO, new InternetAddress(toEmail));
    email.setSubject(subject);
    email.setText(message);

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    email.writeTo(buffer);
    byte[] rawMessageBytes = buffer.toByteArray();
    String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
    Message msg = new Message();
    msg.setRaw(encodedEmail);

    try {
      msg = service.users().messages().send("me", msg).execute();
      log.info("Message to: " + toEmail);
      log.info(msg.toPrettyString());
    } catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403) {
        log.error("Unable to send message: " + e.getDetails());
      } else {
        throw e;
      }
    }
  }

}
