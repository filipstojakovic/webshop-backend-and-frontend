package org.etf.webshopbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.etf.webshopbackend.model.dto.CategoryDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

  //TODO: validations
  private String name;

  private String description;

  private Double price;

  private Boolean isNew;
  private String location;

  private CategoryDto category;

  private String image;
}
