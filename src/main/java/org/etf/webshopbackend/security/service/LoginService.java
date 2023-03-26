package org.etf.webshopbackend.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.BadCredentialsException;
import org.etf.webshopbackend.model.request.LoginRequest;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.security.token.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.etf.webshopbackend.exceptions.UnAuthorizedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  public String login(LoginRequest request) {
    String token;
    try {

      Authentication authenticate = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      token = tokenProvider.createToken(authenticate);

    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new BadCredentialsException("Wrong username or password...");
    }
    return token;
  }

  public Optional<JwtUserDetails> getJwtUserDetailsFromRequest() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }
    return Optional.of(((JwtUserDetails) authentication.getPrincipal()));
  }

  public Long getUserIdFromRequest() {
    JwtUserDetails jwtUserDetails = getJwtUserDetailsFromRequest().orElseThrow(UnAuthorizedException::new);
    return jwtUserDetails.getId();
  }
}
