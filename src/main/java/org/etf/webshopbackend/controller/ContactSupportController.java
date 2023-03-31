package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.request.ContactSupportRequest;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.ContactSupportService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("contact-support")
public class ContactSupportController {

  private final ContactSupportService contactSupportService;

  @PostMapping
  public void sendMessage(@RequestBody ContactSupportRequest request, @AuthenticationPrincipal JwtUserDetails user) {
    contactSupportService.sendMessage(request, user.getId());
  }
}
