package org.etf.webshopbackend.security.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.model.request.LoginRequest;
import org.etf.webshopbackend.security.service.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {

  private final LoginService loginService;

  @PostMapping(value = "/login")
  public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
    String token = loginService.login(loginRequest);
    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, SecurityConstants.AUTH_HEADER + token)
        .build();
  }
}
