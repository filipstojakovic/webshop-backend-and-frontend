package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Pin;
import org.etf.webshopbackend.repository.PinRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class PinService {

  private final PinRepository pinRepository;

  public static final Random random = new Random(System.currentTimeMillis());
  public static final int LOWER_BOUND = 1000;
  public static final int UPPER_BOUND = 10000;

  public Pin generatePinForUser(Long userId) {
    String pinString;
    Optional<Pin> checkPinExists;

    do {
      int pinNumber = LOWER_BOUND + random.nextInt(UPPER_BOUND - LOWER_BOUND);
      pinString = String.valueOf(pinNumber);
      checkPinExists = pinRepository.findByPinAndUser_Id(pinString, userId);

    } while (checkPinExists.isPresent());

    Pin newPin = new Pin();
    newPin.setPin(pinString);
    return newPin;
  }

  public boolean activateUsingPin(String pin, Long userId) {

    //TODO: send mail if pin not valid
    Optional<Pin> checkPinExists = pinRepository.findByPinAndUser_Id(pin, userId);
    //TODO: update user's isActivated if pin matched
    checkPinExists.ifPresent(checkPin -> pinRepository.deleteById(checkPin.getId()));
    return checkPinExists.isPresent();
  }

}
