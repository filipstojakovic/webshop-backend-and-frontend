package org.etf.webshopbackend.model.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.response.ProductAttributesResponse;
import org.etf.webshopbackend.model.response.ProductCategoryResponse;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMapper {

  private final ModelMapper modelMapper;
  private final ProductAttributesMapper productAttributesMapper;
  private final UserMapper userMapper;
  private final FileService fileService;

  public ProductResponse toResponse(Product product) {
    ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
    Category category = product.getCategory();
    productResponse.setProductCategory(new ProductCategoryResponse(category.getId(), category.getName()));

    UserResponse sellerResponse = userMapper.toResponse(product.getSeller(),UserResponse.class);
    productResponse.setSeller(sellerResponse);

    List<ProductAttributesResponse> productAttributesResponse = productAttributesMapper.toResponse(product.getProductHasAttributes());
    productResponse.setProductAttributesResponses(productAttributesResponse);
    setProductImage(product, productResponse);
    return productResponse;
  }

  private void setProductImage(Product product, ProductResponse productResponse) {
    String image = null;
    try {
      image = fileService.loadImageFromPath(product.getImage());
    } catch (IOException e) {
      log.error("unable to load image from path: " + product.getImage());
      product.setImage(null);
    }
    productResponse.setImage(image);
  }
}
