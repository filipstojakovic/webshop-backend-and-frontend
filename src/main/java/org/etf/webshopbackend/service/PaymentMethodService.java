package org.etf.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.PaymentMethod;
import org.etf.webshopbackend.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentMethodService {

  private final PaymentMethodRepository paymentMethodRepository;

  public List<PaymentMethod> findAll() {
    return paymentMethodRepository.findAll();
  }
}
