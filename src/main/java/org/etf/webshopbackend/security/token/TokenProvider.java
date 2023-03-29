package org.etf.webshopbackend.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenProvider {

  @Value("${app.auth.tokenSecret}")
  private String secretKey;

  @Value("${app.auth.tokenExpirationMillis}")
  private Integer tokenExpirationMillis;

  private final UserRepository userRepository;

  @PostConstruct
  protected void init() {
    // this is to avoid having the raw secret key available in the JVM
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(Authentication authentication) {
    JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
    return buildToken(userDetails);
  }

  public String buildToken(JwtUserDetails userDetails) {
    return Jwts.builder()
        .claim(SecurityConstants.ROLE, userDetails.getRole())
        .claim(SecurityConstants.USER_ID, userDetails.getId())
        .claim(SecurityConstants.IS_ACTIVE, userDetails.getIsActive())
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationMillis))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();

    String userIdStr = String.valueOf(claims.get(SecurityConstants.USER_ID));
    return Long.parseLong(userIdStr);
  }

  public boolean validateToken(String jwtToken) {
    try {
      Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(jwtToken);

      // check is deleted in meantime
      Long userId = getUserIdFromToken(jwtToken);
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NotFoundException(User.class, userId));
      if (user.getIsDeleted()) {
        return false;
      }

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
    } catch (Exception ex) {
      log.error("JWT not valid: " + ex.getMessage());
    }

    return false;
  }
}
