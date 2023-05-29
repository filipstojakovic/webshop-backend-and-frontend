package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public Page<ProductResponse> findAllPageable(Pageable page, @RequestParam(required = false) String searchText) {
    return productService.findAllPageable(page, searchText);
  }

  // TODO: delete, use for testing
  @GetMapping("all")
  public List<ProductResponse> findAll() {
    return productService.findAll();
  }

  @GetMapping("{id}")
  public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
    ProductResponse productResponse = productService.findById(id);
    return ResponseEntity.ok(productResponse);
  }

  @PostMapping
  public ResponseEntity<ProductResponse> insert(@RequestBody ProductRequest productRequest,
                                                @AuthenticationPrincipal JwtUserDetails user) {
    ProductResponse productResponse = productService.insert(productRequest, user.getId());
    return ResponseEntity.ok(productResponse);
  }

  @Async
  @GetMapping("{id}/image")
  public CompletableFuture<ResponseEntity<byte[]>> getProductImage(@PathVariable("id") Long imageId) {
    log.info("Current Thread Name: " + Thread.currentThread().getName());
    var image = productService.getProductImage(imageId);
    return CompletableFuture.completedFuture(ResponseEntity.ok(image));
  }

}
