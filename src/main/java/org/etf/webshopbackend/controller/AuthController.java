package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.model.request.LoginRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.security.service.AuthService;
import org.etf.webshopbackend.service.PinService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthService authService;
  private final PinService pinService;

  @PostMapping("/login")
  public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest) {
    String token = authService.login(loginRequest);
    try {
      pinService.sendMailIfNotActivated(loginRequest.getUsername());
    } catch (Exception ex) {
      log.error("Unable to send pin to email");
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, SecurityConstants.AUTH_HEADER + token)
        .build();
  }

  @PostMapping(path = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<UserResponse> register(@ModelAttribute @Valid MultipartFile file) {

    return null;
//     UserResponse createdUser = authService.register(user);
    // TODO: maybe send pin via email
    // maybe add token
//     return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }
}
