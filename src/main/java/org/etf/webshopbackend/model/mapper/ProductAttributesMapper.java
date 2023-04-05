package org.etf.webshopbackend.model.mapper;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.ProductHasAttribute;
import org.etf.webshopbackend.model.response.ProductAttributesResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAttributesMapper {

  private final ModelMapper modelMapper;

  public List<ProductAttributesResponse> toResponse(List<ProductHasAttribute> productHasAttributes) {
    return productHasAttributes.stream()
        .map(productHasAttribute -> {
          Long attributeId = productHasAttribute.getAttribute().getId();
          String attributeName = productHasAttribute.getAttribute().getName();
          String value = productHasAttribute.getValue();
          return new ProductAttributesResponse(attributeId, attributeName, value);
        }).toList();
  }

}
