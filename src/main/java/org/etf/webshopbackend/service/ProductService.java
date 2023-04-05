package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Attribute;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.ProductHasAttribute;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.mapper.ProductMapper;
import org.etf.webshopbackend.model.request.AttributeNameValueRequest;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.repository.AttributeRespository;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.security.service.TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

  private final TokenService tokenService;
  private final GenericMapper<ProductRequest, Product, ProductResponse> genericProductMapper;
  private final ProductMapper productMapper;
  private final AttributeRespository attributeRespository;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;
  private final FileService fileService;

  //       .param("page", "5")
//         .param("size", "10")
//         .param("sort", "id,desc")   // <-- no space after comma!
//         .param("sort", "name,asc")) // <-- no
  public Page<Product> findAllPageable(Pageable page) {

    Page<Product> products = productRepository.findAll(page).map(product -> {
      try {
        product.setImage(fileService.loadImageFromPath(product.getImage()));
      } catch (IOException e) {
        product.setImage(null);
      }
      return product;
    });
    return products;
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public ProductResponse insert(ProductRequest productRequest) {
    String imagePath = null;
    try {
      if (productRequest.getImage() != null) {
        imagePath = fileService.saveBase64String(productRequest.getImage());
      }
    } catch (IOException ex) {
      log.error("faild to upload product image");
      ex.printStackTrace();
    }
    Product product = genericProductMapper.fromRequest(productRequest, Product.class);
    Long userId = tokenService.getUserIdFromRequest();
    User sellerUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    Long categoryId = productRequest.getCategory().getId();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException(Category.class, categoryId));
    product.setCategory(category);
    product.setSeller(sellerUser);
    product.setImage(imagePath);
    try {
      product = productRepository.saveAndFlush(product);
      List<ProductHasAttribute> productHasAttributes = createProductAttributeList(product, productRequest.getCategory()
          .getAttributes());
      product.getProductHasAttributes().addAll(productHasAttributes); // TODO: mozda get pa set?

    } catch (Exception ex) {
      // delete image if failed to insert product
      try {
        fileService.deleteFile(imagePath);
      } catch (IOException e) {
        log.error("faild to delete product image");
        throw new RuntimeException(e);
      }
      ex.printStackTrace();
      throw ex;
    }

    return productMapper.toResponse(product);
  }

  private List<ProductHasAttribute> createProductAttributeList(Product product, final List<AttributeNameValueRequest> attributes) {
    if (attributes == null) {
      return Collections.emptyList();
    }

    Long categoryId = product.getCategory().getId();
    return attributes.stream()
        .filter(attributeNameValueRequest ->
            attributeNameValueRequest.getValue() != null)
        .map((attributeNameValueRequest -> {
          Attribute attribute = attributeRespository.findAttributeByCategory_IdAndName(categoryId, attributeNameValueRequest.getName())
              .orElseThrow(() -> new NotFoundException("Attribute not found with: categoryId: " + categoryId + " and name: " + attributeNameValueRequest.getName()));

          String value = attributeNameValueRequest.getValue();
          return new ProductHasAttribute(product, attribute, value);
        }
        )).toList();
  }
}
