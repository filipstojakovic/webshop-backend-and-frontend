package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.security.service.TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

  private final TokenService tokenService;
  private final GenericMapper<ProductRequest, Product, ProductResponse> productMapper;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;
  private final FileService fileService;

  //       .param("page", "5")
//         .param("size", "10")
//         .param("sort", "id,desc")   // <-- no space after comma!
//         .param("sort", "name,asc")) // <-- no
  public Page<Product> findAll(Pageable page) {
    return productRepository.findAll(page);
  }

  public ProductResponse insert(ProductRequest productRequest) {
    String imagePath = null;
    try {
      imagePath = fileService.saveBase64String(productRequest.getImage());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    Long userId = tokenService.getUserIdFromRequest();
    User sellerUser = userRepository.findById(userId).orElseThrow(()->new NotFoundException(User.class,userId));
    Long categoryId = productRequest.getCategory().getId();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException(Category.class, categoryId));
    Product product = productMapper.fromRequest(productRequest, Product.class);
    product.setCategory(category);
    product.setSeller(sellerUser);
    product.setImage(imagePath);
    try {
      productRepository.saveAndFlush(product);
    }catch(Exception ex)
    {
      try {
        fileService.deleteFile(imagePath);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      ex.printStackTrace();
       throw ex;
    }


    return productMapper.toResponse(product, ProductResponse.class);
  }
}
