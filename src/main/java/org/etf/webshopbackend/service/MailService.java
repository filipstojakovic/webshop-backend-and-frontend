package org.etf.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

  @Value("${app.email}")
  private String email;

  @Async
  public void sendSimpleMessage(String to, String subject, String messageText)  {
  }
}
