package org.etf.webshopbackend.model.mapper;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.response.ProductAttributesResponse;
import org.etf.webshopbackend.model.response.ProductCategoryResponse;
import org.etf.webshopbackend.model.response.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
  private final ModelMapper modelMapper;
  private final ProductAttributesMapper productAttributesMapper;

  public ProductResponse toResponse(Product product){
    ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
    Category category = product.getCategory();
    productResponse.setProductCategory(new ProductCategoryResponse(category.getId(),category.getName()));

    List<ProductAttributesResponse> productAttributesResponse = productAttributesMapper.toResponse(product.getProductHasAttributes());
    productResponse.setProductAttributesResponses(productAttributesResponse);
    return productResponse;
  }
}
