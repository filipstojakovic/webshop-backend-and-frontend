package org.etf.webshopbackend.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.security.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      final String authorizationHeader = request.getHeader(SecurityConstants.HEADER);
      if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstants.AUTH_HEADER)) {
        filterChain.doFilter(request, response);
        return;
      }
      String jwtToken = authorizationHeader.replace(SecurityConstants.AUTH_HEADER, "").trim();

      if (StringUtils.hasText(jwtToken) && tokenProvider.validateToken(jwtToken)) {
        Long userId = tokenProvider.getUserIdFromToken(jwtToken);

        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        ((JwtUserDetails) userDetails).setPassword(null); // we don't want AuthenticationPrincipal to have password
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      log.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }
}
