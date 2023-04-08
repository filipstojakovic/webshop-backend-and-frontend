package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Purchase;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.response.PurchaseResponse;
import org.etf.webshopbackend.repository.PurchaseRepository;
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

  // TODO: only can get his own purchases

  //@AuthenticationPrincipal JwtUserDetails user
  @GetMapping
  public List<PurchaseResponse> findAllPurchasedProducts() {
    List<Purchase> purchases = purchaseRepository.findByUserId(1L);
    return purchaseMapper.toResponses(purchases, PurchaseResponse.class);
  }

}
