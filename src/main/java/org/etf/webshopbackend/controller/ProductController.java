package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public Page<ProductResponse> findAllPageable(Pageable page) {
    return productService.findAllPageable(page);
  }

  @GetMapping("all")
  public List<Product> findAll() {
    return productService.findAll();
  }

  @PostMapping
  public ResponseEntity<ProductResponse> insert(@RequestBody ProductRequest productRequest) {
    ProductResponse productResponse = productService.insert(productRequest);

    return ResponseEntity.ok(productResponse);
  }
}
