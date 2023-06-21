package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.request.PurchaseRequest;
import org.etf.webshopbackend.model.response.PurchaseResponse;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PurchaseController {

  private final PurchaseService purchaseService;

  @GetMapping("purchases")
  public ResponseEntity<List<PurchaseResponse>> findUserPurchaseHistory(@AuthenticationPrincipal JwtUserDetails user) {
    List<PurchaseResponse> purchasesResponse = purchaseService.findById(user.getId());
    return ResponseEntity.ok(purchasesResponse);
  }

  @PostMapping("products/{id}/purchases")
  public ResponseEntity<PurchaseResponse> purchaseProduct(@PathVariable(name = "id") Long productId,
                                                          @RequestBody PurchaseRequest purchaseRequest,
                                                          @AuthenticationPrincipal JwtUserDetails user) {
    PurchaseResponse purchaseResponse = purchaseService.purchaseProduct(productId,purchaseRequest, user.getId());
    return ResponseEntity.ok(purchaseResponse);
  }
}
