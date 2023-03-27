package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.model.request.LoginRequest;
import org.etf.webshopbackend.security.service.LoginService;
import org.etf.webshopbackend.service.PinService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

  private final LoginService loginService;
  private final PinService pinService;

  @PostMapping(value = "/login")
  public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
    String token = loginService.login(loginRequest);
    try {
      pinService.sendMailIfNotActivated(loginRequest.getUsername());
    } catch (Exception ex) {
      log.error("Unable to send pin to email");
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, SecurityConstants.AUTH_HEADER + token)
        .build();
  }
}
