package org.etf.webshopbackend.security;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.constants.EndpointConstants;
import org.etf.webshopbackend.model.enums.RoleEnum;
import org.etf.webshopbackend.security.service.CustomUserDetailsService;
import org.etf.webshopbackend.security.token.TokenAuthenticationFilter;
import org.etf.webshopbackend.security.token.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final TokenProvider tokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                                           UserDetailsService userDetailService) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailService)
        .passwordEncoder(passwordEncoder)
        .and()
        .build();
  }

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService);
  }

  @Profile("local")
  @Bean // disable security
  public SecurityFilterChain noSecurityfilterChain(HttpSecurity http) throws Exception {
    http.csrf().and().cors().disable();
    http.authorizeHttpRequests().requestMatchers("/**").permitAll();
    http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/**").permitAll();
    return http.build();
  }

  @Profile("local")
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers("/**");
  }

  @Profile("default")
  @Bean // this will only run in dev (normal) profile
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    createAuthorizationRules(http);
    http.cors()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .exceptionHandling()
        .and()
        .authorizeHttpRequests()
        .anyRequest()
        .authenticated()
        .and()
        .logout();

    // Our custom Token based authentication filter
    http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  private void createAuthorizationRules(HttpSecurity http) throws Exception {
    publicAuthorizationRule(http);
    userAuthorizationRule(http);
    // TODO: authrorize other endpoints
  }

  private void publicAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.OPTIONS, EndpointConstants.ALL_PATHS)
        .permitAll();
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST, EndpointConstants.LOGIN)
        .permitAll();
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/user/me")
        .permitAll();
    http.authorizeHttpRequests()
        .requestMatchers("/logout")
        .permitAll();
  }

  private void userAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, EndpointConstants.USERS)
        .hasAnyAuthority(RoleEnum.user.toString(), RoleEnum.admin.toString())
        .requestMatchers(HttpMethod.POST, EndpointConstants.USERS)
        .hasAnyAuthority(RoleEnum.admin.toString())
        .requestMatchers(HttpMethod.PUT, EndpointConstants.USERS)
        .hasAnyAuthority(RoleEnum.admin.toString())
        .requestMatchers(HttpMethod.DELETE, EndpointConstants.USERS)
        .hasAnyAuthority(RoleEnum.admin.toString());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
