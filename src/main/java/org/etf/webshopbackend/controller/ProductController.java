package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.request.SearchProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public List<ProductResponse> findAll() {
    return productService.findAll();
  }

  @PostMapping("search")
  public Page<ProductResponse> findAllPageable(Pageable page, @RequestBody(required = false) SearchProductRequest searchProductRequest) {
    return productService.findAllPageable(page, searchProductRequest);
  }

  @PostMapping("purchase-history/search")
  public Page<ProductResponse> findAllUserPurchaseHistoryPageable(Pageable page,
                                                                  @RequestBody(required = false) SearchProductRequest searchProductRequest,
                                                                  @AuthenticationPrincipal JwtUserDetails user) {
    return productService.findAllUserPurchaseHistoryPageable(page, searchProductRequest, user.getId());
  }

  @PostMapping("user/search")
  public Page<ProductResponse> findAllUserProductsPageable(Pageable page,
                                                           @RequestBody(required = false) SearchProductRequest searchProductRequest,
                                                           @AuthenticationPrincipal JwtUserDetails user) {
    return productService.findAllUserProductsPageable(page, searchProductRequest, user.getId());
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
    log.info("product with id: " + productResponse.getId() + " created");
    return ResponseEntity.ok(productResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id,
                                     @AuthenticationPrincipal JwtUserDetails user) {
    productService.delete(id, user.getId());
    log.info("product with id: " + id + " deleted");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Async
  @GetMapping("{id}/image")
  public CompletableFuture<ResponseEntity<byte[]>> getProductImage(@PathVariable("id") Long imageId) {
    var image = productService.getProductImage(imageId);
    return CompletableFuture.completedFuture(ResponseEntity.ok(image));
  }

}
