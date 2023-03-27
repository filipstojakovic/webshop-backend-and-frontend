package org.etf.webshopbackend.security.service;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.UnAuthorizedException;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

  public Optional<JwtUserDetails> getJwtUserDetailsFromRequest() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }
    Object participant = authentication.getPrincipal();
    return Optional.of(((JwtUserDetails) participant));
  }

  public Long getUserIdFromRequest() {
    JwtUserDetails jwtUserDetails = getJwtUserDetailsFromRequest().orElseThrow(UnAuthorizedException::new);
    return jwtUserDetails.getId();
  }
}
