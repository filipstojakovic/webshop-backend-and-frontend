package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.NotAuthenticatedException;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.security.service.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class WhoAmI {

  private final LoginService loginService;

  @RequestMapping("user/me")
  public JwtUserDetails user() {
    return loginService.getJwtUserDetailsFromRequest().orElseThrow(NotAuthenticatedException::new);
  }
}
