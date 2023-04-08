package org.etf.webshopbackend.security.config;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.advice.RestExceptionHandler;
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
import org.springframework.security.config.core.GrantedAuthorityDefaults;
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
  public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailService) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailService)
        .passwordEncoder(passwordEncoder).and().build();
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
    // @formatter:off
    http
        .cors()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests().anyRequest().denyAll();
    // @formatter:on

    http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling().authenticationEntryPoint(new RestExceptionHandler());

    return http.build();
  }

  private void createAuthorizationRules(HttpSecurity http) throws Exception {
    userAuthorizationRule(http);
    publicAuthorizationRule(http);
    activatePinAuthorizationRule(http);
    categoryAuthorizationRule(http);
    procutAuthorizationRule(http);
    contactSupportAuthorizationRule(http);
    purchaseAuthorizationRule(http);
    // TODO: authrorize other endpoints
  }

  private void categoryAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, EndpointConstants.CATEGORIES)
        .permitAll();
  }

  private void contactSupportAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST, EndpointConstants.CONTACT_SUPPORT)
        .hasAnyAuthority(RoleEnum.user.toString());
  }

  private void activatePinAuthorizationRule(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST, EndpointConstants.PIN)
        .hasAnyAuthority(RoleEnum.user.toString(), RoleEnum.admin.toString());
  }

  private void publicAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS, EndpointConstants.ALL_PATHS).permitAll();
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST, EndpointConstants.LOGIN)
        .permitAll()
        .requestMatchers(HttpMethod.POST, EndpointConstants.REGISTER)
        .permitAll()
        .requestMatchers(HttpMethod.GET, EndpointConstants.WHOAMI)
        .permitAll();
  }

  private void userAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests ->
        requests
            .requestMatchers(HttpMethod.GET, EndpointConstants.USERS + EndpointConstants.ALL_PATHS)
            .permitAll()
            //TODO: permit post and put but only for them selfs
            .requestMatchers(HttpMethod.POST, EndpointConstants.USERS)
            .hasAnyAuthority(RoleEnum.admin.toString())
            .requestMatchers(HttpMethod.PUT, EndpointConstants.USERS)
            .hasAnyAuthority(RoleEnum.admin.toString())
            .requestMatchers(HttpMethod.DELETE, EndpointConstants.USERS)
            .hasAnyAuthority(RoleEnum.admin.toString())
    );
  }

  private void procutAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests ->
        requests
            .requestMatchers(HttpMethod.GET, EndpointConstants.PRODUCTS + EndpointConstants.ALL_PATHS)
            .permitAll()
            .requestMatchers(HttpMethod.POST, EndpointConstants.PRODUCTS + EndpointConstants.ALL_PATHS)
            .hasAnyAuthority(RoleEnum.user.name(), RoleEnum.admin.name()));
  }

  private void purchaseAuthorizationRule(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests ->
        requests
            .requestMatchers(HttpMethod.GET, EndpointConstants.PURCHASES + EndpointConstants.ALL_PATHS)
            .hasAnyAuthority(RoleEnum.user.name(), RoleEnum.admin.name())
            .requestMatchers(HttpMethod.POST, EndpointConstants.PURCHASES + EndpointConstants.ALL_PATHS)
            .hasAnyAuthority(RoleEnum.user.name(), RoleEnum.admin.name()));
  }

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
