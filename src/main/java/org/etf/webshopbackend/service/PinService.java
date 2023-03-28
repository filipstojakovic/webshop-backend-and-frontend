package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Pin;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.repository.PinRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.security.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class PinService {

  private final UserRepository userRepository;
  private final PinRepository pinRepository;
  private final TokenService tokenService;
  private final MailService mailService;

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

    pinRepository.deleteAllByUser_Id(user.getId());
    Pin newPin = generatePinForUser(user);
    pinRepository.saveAndFlush(newPin);
    mailService.sendMailAsync(user.getEmail(), "Novi pin", "Pin: " + newPin.getPin());
  }

  public void activateUsingPin(String pin, Long userId) {

    Optional<Pin> checkPinExists = pinRepository.findByPinAndUser_Id(pin, userId);
    if (!checkPinExists.isPresent()) {
      // TODO: send new mail
      throw new BadRequestException("Pin does not match");
    }
    userRepository.findById(userId).ifPresent(user -> user.setIsActive(true));
    checkPinExists.ifPresent(checkPin -> pinRepository.deleteAllByUser_Id(userId));
  }

}
