package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Attribute;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.ProductHasAttribute;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.mapper.ProductMapper;
import org.etf.webshopbackend.model.request.AttributeNameValueRequest;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.repository.AttributeRespository;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.UserRepository;
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
  public Page<ProductResponse> findAllPageable(Pageable page) {
    return productRepository.findAll(page)
        .map(productMapper::toResponse);
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll()
        .stream()
        .map(productMapper::toResponse)
        .toList();
  }

  public ProductResponse insert(ProductRequest productRequest, Long userId) {
    User sellerUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    Product product = productMapper.fromRequest(productRequest);
    product.setSeller(sellerUser);

    try {
      product = productRepository.saveAndFlush(product);
      List<ProductHasAttribute> productHasAttributes = createProductAttributeList(product, productRequest.getCategory()
          .getAttributes());
      product.getProductHasAttributes().addAll(productHasAttributes);

    } catch (Exception ex) {
      // delete image if failed to insert product
      try {
        fileService.deleteFile(product.getImage());
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

  public ProductResponse findById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Product.class, id));
    return productMapper.toResponse(product);
  }
}
