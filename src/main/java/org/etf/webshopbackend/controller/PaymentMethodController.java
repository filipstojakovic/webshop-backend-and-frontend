package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.PaymentMethod;
import org.etf.webshopbackend.service.PaymentMethodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("payment-methods")
public class PaymentMethodController {

  private final PaymentMethodService paymentMethodService;

  @GetMapping
  public List<PaymentMethod> findAll() {
    return paymentMethodService.findAll();
  }
}
