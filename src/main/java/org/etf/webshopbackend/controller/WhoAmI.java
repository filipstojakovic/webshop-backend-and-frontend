package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.UnAuthorizedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class WhoAmI {

  @RequestMapping("user/me")
  public Principal user(Principal user) {
    return Optional.of(user).orElseThrow(UnAuthorizedException::new);
  }

}
