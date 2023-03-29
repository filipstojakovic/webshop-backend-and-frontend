package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Pin;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.repository.PinRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.security.service.TokenService;
import org.etf.webshopbackend.security.token.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PinService {

  private final UserRepository userRepository;
  private final PinRepository pinRepository;
  private final TokenService tokenService;
  private final MailService mailService;
  private final TokenProvider tokenProvider;

  public static final Random random = new Random(System.currentTimeMillis());
  public static final int LOWER_BOUND = 1000;
  public static final int UPPER_BOUND = 10000;

  public Pin generatePinForUser(User user) {
    String pinString;
    Optional<Pin> checkPinExists;

    do {
      int pinNumber = LOWER_BOUND + random.nextInt(UPPER_BOUND - LOWER_BOUND);
      pinString = String.valueOf(pinNumber);
      checkPinExists = pinRepository.findByPinAndUser_Id(pinString, user.getId());

    } while (checkPinExists.isPresent());

    Pin newPin = new Pin();
    newPin.setPin(pinString);
    newPin.setUser(user);

    return newPin;
  }

  public void sendMailIfNotActivated(String username) throws Exception {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found by username"));
    boolean isActivated = user.getIsActive();
    if (isActivated) {
      return;
    }

    removeUsersPins(user.getId());
    Pin newPin = generatePinForUser(user);
    pinRepository.saveAndFlush(newPin);
    mailService.sendMailAsync(user.getEmail(), "Novi pin", "Pin: " + newPin.getPin());
  }

  public String activateUsingPin(String pin, Long userId) {

    User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    Optional<Pin> checkPinExists = pinRepository.findByPinAndUser_Id(pin, userId);
    if (checkPinExists.isEmpty()) {
      try {
        // TODO: refactor this, exception is triggering rollback transaction
        sendMailIfNotActivated(user.getUsername());
      } catch (Exception e) {
        log.error("Unable to send pin to email");
      }
      throw new BadRequestException("Pin does not match");
    }

    user.setIsActive(true);
    checkPinExists.ifPresent(checkPin -> removeUsersPins(userId));
    return tokenProvider.buildToken(JwtUserDetails.create(user));
  }

  private void removeUsersPins(Long userId) {
    pinRepository.deleteAllByUser_Id(userId);
  }

}
