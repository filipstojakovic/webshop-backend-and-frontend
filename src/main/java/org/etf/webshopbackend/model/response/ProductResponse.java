package org.etf.webshopbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

  private Long id;
  private String name;
  private String description;
  private Double price;
  private String location;
  private Boolean isNew;
  private UserResponse seller;
  private List<ProductImageResponse> productImages;
  private ProductCategoryResponse category;
  private List<ProductAttributesResponse> productAttributes;
}
