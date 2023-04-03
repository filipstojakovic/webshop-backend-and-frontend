package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public Page<Product> findAll(Pageable page) {
    return productService.findAll(page);
  }

  @PostMapping(path = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<UserResponse> insert(@ModelAttribute ProductRequest productRequest,
                                               @RequestParam(required = false) MultipartFile image) {
    System.out.println();

    return  null;
  }
}
