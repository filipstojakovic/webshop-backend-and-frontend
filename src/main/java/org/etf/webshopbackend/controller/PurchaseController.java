package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Purchase;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.response.PurchaseResponse;
import org.etf.webshopbackend.repository.PurchaseRepository;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("purchases")
public class PurchaseController {

  private final PurchaseRepository purchaseRepository;
  private final GenericMapper<Purchase, Purchase, PurchaseResponse> purchaseMapper;

  @GetMapping
  public List<PurchaseResponse> findUserPurchaseHistory(@AuthenticationPrincipal JwtUserDetails user) {
    List<Purchase> purchases = purchaseRepository.findByUserId(user.getId());
    return purchaseMapper.toResponses(purchases, PurchaseResponse.class);
  }

}
