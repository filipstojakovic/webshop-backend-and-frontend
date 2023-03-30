package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

  private final ProductRepository productRepository;

//       .param("page", "5")
//         .param("size", "10")
//         .param("sort", "id,desc")   // <-- no space after comma!
//         .param("sort", "name,asc")) // <-- no
  public Page<Product> findAll(Pageable page) {
    return productRepository.findAll(page);
  }
}
