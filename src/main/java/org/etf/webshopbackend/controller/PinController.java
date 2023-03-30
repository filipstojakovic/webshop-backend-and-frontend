package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.constants.SecurityConstants;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.etf.webshopbackend.model.request.ActivationPinRequest;
import org.etf.webshopbackend.security.service.TokenService;
import org.etf.webshopbackend.service.PinService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("pins")
public class PinController {

  private final PinService pinService;
  private final TokenService tokenService;

  @PostMapping
  public ResponseEntity<Void> activatePin(@Valid @RequestBody ActivationPinRequest activationPinRequest) throws Exception {
    Long userId = tokenService.getUserIdFromRequest();
    String newToken = null;
    try {
      newToken = pinService.activateUsingPin(activationPinRequest.getPin(), userId);
    } catch (BadRequestException ex) {
      log.info("Sending new token via email");
      pinService.sendNewPin(userId);
      throw ex;
    }

    var response = ResponseEntity.ok();
    if (newToken != null) {
      response.header(HttpHeaders.AUTHORIZATION, SecurityConstants.AUTH_HEADER + newToken);
    }
    return response.build();
  }

}
