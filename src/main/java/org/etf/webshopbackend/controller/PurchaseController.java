package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Product;
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

  @GetMapping()
  public List<Product> findAllPurchasedProducts(@AuthenticationPrincipal JwtUserDetails user){

    //TODO: not implemented
    return null;
  }
}
