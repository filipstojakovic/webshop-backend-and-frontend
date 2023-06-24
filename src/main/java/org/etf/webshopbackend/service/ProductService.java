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
import org.etf.webshopbackend.model.request.SearchProductRequest;
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
  //       .param("size", "10")
  //       .param("sort", "id,desc")   // <-- no space after comma!
  //       .param("sort", "name,asc")) //
  public Page<ProductResponse> findAllPageable(Pageable page, SearchProductRequest searchProductRequest) {

    Pageable sortedPage = defaultSortPageIfNotExists(page);

    Specification<Product> searchProductSpecification = createSpecificationFromRequest(searchProductRequest);
    searchProductSpecification = ProductSpecifications.notPurchased().and(searchProductSpecification);

    return productRepository.findAll(searchProductSpecification, sortedPage)
        .map(productMapper::toResponse);
  }

  // users purchase history
  public Page<ProductResponse> findAllUserPurchaseHistoryPageable(Pageable page,
                                                                  SearchProductRequest searchProductRequest,
                                                                  Long userId) {

    Pageable sortedPage = defaultSortPageIfNotExists(page);

    Specification<Product> searchProductSpecification = createSpecificationFromRequest(searchProductRequest);
    searchProductSpecification = ProductSpecifications.userPurchased(userId).and(searchProductSpecification);

    return productRepository.findAll(searchProductSpecification, sortedPage)
        .map(productMapper::toResponse);
  }

  // prdocuts that user is selling
  public Page<ProductResponse> findAllUserProductsPageable(Pageable page,
                                                           SearchProductRequest searchProductRequest,
                                                           Long userId) {
    Pageable sortedPage = defaultSortPageIfNotExists(page);

    Specification<Product> searchProductSpecification = createSpecificationFromRequest(searchProductRequest);
    searchProductSpecification = ProductSpecifications.userSelling(userId).and(searchProductSpecification);

    return productRepository.findAll(searchProductSpecification, sortedPage)
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

  private Specification<Product> createSpecificationFromRequest(SearchProductRequest searchProductRequest) {
    if (searchProductRequest == null) {
      return ProductSpecifications.emptySpecification();
    }

    Specification<Product> productNameSpecification = ProductSpecifications.productByName(searchProductRequest.getNameSearch()
        .trim());
    if (searchProductRequest.getCategoryIdSearch() == null) {
      return productNameSpecification;
    }

    Specification<Product> productCategorySpecification = ProductSpecifications.byCategoryId(searchProductRequest.getCategoryIdSearch());
    Specification<Product> productAttributeSpecificaiton = createSpecificationFromArray(searchProductRequest.getAttributeNameValueSearches());
    return productNameSpecification
        .and(productCategorySpecification)
        .and(productAttributeSpecificaiton);
  }

  private Specification<Product> createSpecificationFromArray(List<AttributeNameValueRequest> attributeNameValueRequests) {
    if (attributeNameValueRequests == null || attributeNameValueRequests.isEmpty()) {
      return ProductSpecifications.emptySpecification();
    }

    return attributeNameValueRequests.stream()
        .map(attributeNameValueRequest ->
            ProductSpecifications.hasAttributeWithValue(attributeNameValueRequest.getName(),
                attributeNameValueRequest.getValue().trim()))
        .reduce(Specification::and).orElseThrow(BadRequestException::new);

  }

  private Pageable defaultSortPageIfNotExists(Pageable page) {
    return page.getSort().isSorted() ? page : PageRequest.of(
        page.getPageNumber(),
        page.getPageSize(),
        Sort.by(DEFAULT_SORT_COLUMN).descending());
  }

}
