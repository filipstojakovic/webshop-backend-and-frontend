package org.etf.webshopbackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  private final static long MAX_AGE_SECS = 3600;

  @Value("${app.cors.allowedOrigins}")
  private String[] allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
//         .allowedOriginPatterns("http://localhost:*")
        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name())
        .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "X-XSRF-TOKEN", HttpHeaders.ACCEPT)
        .exposedHeaders(CorsConfiguration.ALL)
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
  }
}
