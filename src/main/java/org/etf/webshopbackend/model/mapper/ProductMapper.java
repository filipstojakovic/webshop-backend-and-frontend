package org.etf.webshopbackend.model.mapper;

import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.ProductImage;
import org.etf.webshopbackend.model.request.ProductRequest;
import org.etf.webshopbackend.model.response.ProductAttributesResponse;
import org.etf.webshopbackend.model.response.ProductCategoryResponse;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.etf.webshopbackend.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ProductMapper extends GenericMapper<ProductRequest, Product, ProductResponse> {

  private final ProductAttributesMapper productAttributesMapper;
  private final UserMapper userMapper;
  private final FileService fileService;
  private final CategoryRepository categoryRepository;

  public ProductMapper(final ModelMapper modelMapper,
                       final ProductAttributesMapper productAttributesMapper,
                       final UserMapper userMapper,
                       final FileService fileService,
                       final CategoryRepository categoryRepository) {
    super(modelMapper);
    this.productAttributesMapper = productAttributesMapper;
    this.userMapper = userMapper;
    this.fileService = fileService;
    this.categoryRepository = categoryRepository;
  }

  public ProductResponse toResponse(Product product) {
    ProductResponse productResponse = super.toResponse(product, ProductResponse.class);
    Category category = product.getCategory();
    productResponse.setCategory(new ProductCategoryResponse(category.getId(), category.getName()));

    UserResponse sellerResponse = userMapper.toResponse(product.getSeller(), UserResponse.class);
    productResponse.setSeller(sellerResponse);

    List<ProductAttributesResponse> productAttributesResponse = productAttributesMapper.toResponse(product.getProductHasAttributes());
    productResponse.setProductAttributes(productAttributesResponse);
    return productResponse;
  }

  public Product fromRequest(ProductRequest productRequest) {
    Product product = super.fromRequest(productRequest, Product.class);

    List<ProductImage> productImages = saveProductImages(productRequest);
    product.setProductImages(productImages);

    Long categoryId = productRequest.getCategory().getId();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException(Category.class, categoryId));
    product.setCategory(category);

    return product;
  }

  private List<ProductImage> saveProductImages(ProductRequest productRequest) {
    List<ProductImage> productImages = new ArrayList<>();
    for (String image : productRequest.getImages()) {
      try {
        String imagePath = fileService.saveBase64ImageGetPath(image);
        productImages.add(new ProductImage(null, imagePath));
      } catch (IOException e) {
        log.error("Error saving product image");
      }
    }
    return productImages;
  }

}
