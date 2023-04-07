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

  public Pin savePin(Pin pin) {
    return pinRepository.saveAndFlush(pin);
  }

  private void sendMailIfNotActivated(String username, Pin pin) throws Exception {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found by username"));
    boolean isActivated = user.getIsActive();
    if (isActivated) {
      log.info("User already active");
      return;
    }
    mailService.sendMailAsync(user.getEmail(), "Novi pin", "Pin: " + pin.getPin());
  }

  public String activateUsingPin(String pin, Long userId) {

    User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    Optional<Pin> checkPinExists = pinRepository.findByPinAndUser_Id(pin, userId);
    if (checkPinExists.isEmpty()) {
      throw new BadRequestException("Pin does not match");
    }

    user.setIsActive(true);
    checkPinExists.ifPresent(checkPin -> removeUsersPins(userId));
    return tokenProvider.buildToken(JwtUserDetails.create(user));
  }

  public void removeUsersPins(Long userId) {
    pinRepository.deleteAllByUser_Id(userId);
  }

  public void sendNewPin(String username) throws Exception {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found by username"));
    removeUsersPins(user.getId());
    Pin pin = generatePinForUser(user);
    pin = savePin(pin);
    sendMailIfNotActivated(username, pin);
  }

  public void sendNewPin(Long userId) throws Exception {
    User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException(User.class,userId));
    sendNewPin(user.getUsername());
  }
}
