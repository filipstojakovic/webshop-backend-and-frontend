package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.*;
import org.etf.webshopbackend.model.mapper.ProductMapper;
import org.etf.webshopbackend.model.request.AttributeNameValueRequest;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.repository.AttributeRespository;
import org.etf.webshopbackend.repository.ProductImageRepository;
import org.etf.webshopbackend.repository.ProductRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.specification.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
  private final UserRepository userRepository;
  private final FileService fileService;
  private final ProductImageRepository productImageRepository;
  private final static String DEFAULT_SORT_COLUMN = "date";

  //       .param("page", "5")
//         .param("size", "10")
//         .param("sort", "id,desc")   // <-- no space after comma!
//         .param("sort", "name,asc")) //
  public Page<ProductResponse> findAllPageable(Pageable page, String searchText) {
    Pageable sortedPage = defaultSortPageIfNotExists(page);
    Specification<Product> productByName = ProductSpecifications.productByName(searchText);
    Specification<Product> productByCategoryName = ProductSpecifications.byCategoryName(searchText); // testing
    Specification<Product> productByAttributeValue = ProductSpecifications.byAttributeNameAndValue("gramaza",searchText); // testing

    return productRepository.findAll(ProductSpecifications.notPurchased().and(productByAttributeValue), sortedPage)
        .map(productMapper::toResponse);
  }

  // TODO: this is for testing purposes
  public List<ProductResponse> findAll() {
    return productRepository.findAll()
        .stream()
        .map(productMapper::toResponse)
        .toList();
  }

  public ProductResponse insert(ProductRequest productRequest, Long userId) {
    User sellerUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    Product product = productMapper.fromRequest(productRequest); // TODO: extract save images from this method
    product.setSeller(sellerUser);

    try {
      product = productRepository.saveAndFlush(product);
      List<ProductHasAttribute> productHasAttributes = createProductAttributeList(product, productRequest.getCategory()
          .getAttributes());
      product.getProductHasAttributes().addAll(productHasAttributes);

    } catch (Exception ex) {
      product.getProductImages().forEach(x -> {
        try {
          fileService.deleteFile(x.getImagePath());
        } catch (IOException e) {
        }
      });
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

  public byte[] getProductImage(Long productImageId) {

    ProductImage productImage = productImageRepository.findById(productImageId)
        .orElseThrow(() -> new NotFoundException(ProductImage.class, productImageId));

    try {
      return fileService.loadImageBytesFromPath(productImage.getImagePath());
    } catch (IOException e) {
      log.error("Unable to load product image");
      e.printStackTrace();
      throw new BadRequestException("Unable to load image");
    }
  }

  private Pageable defaultSortPageIfNotExists(Pageable page) {
    return page.getSort().isSorted() ? page : PageRequest.of(
        page.getPageNumber(),
        page.getPageSize(),
        Sort.by(DEFAULT_SORT_COLUMN).descending());
  }
}
