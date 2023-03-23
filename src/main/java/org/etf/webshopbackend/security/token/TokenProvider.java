package org.etf.webshopbackend.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TokenProvider {

  @Value("${app.auth.tokenSecret}")
  private String tokenSecret;

  @Value("${app.auth.tokenExpirationMillis}")
  private Integer tokenExpirationMillis;

  public String createToken(Authentication authentication) {
    JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

    return Jwts.builder()
        .claim(SecurityConstants.ROLES, userDetails.getRole())
        .claim(SecurityConstants.USER_ID, userDetails.getId())
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationMillis))
        .signWith(SignatureAlgorithm.HS512, tokenSecret)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(tokenSecret)
        .parseClaimsJws(token)
        .getBody();

    String userIdStr = String.valueOf(claims.get(SecurityConstants.USER_ID));
    return Long.parseLong(userIdStr);
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }
}
