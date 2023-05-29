package org.etf.webshopbackend.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.etf.webshopbackend.model.dto.CategoryDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

  @NotNull
  @NotBlank
  private String name;
  private String description;
  @NotNull
  @NotBlank
  private Double price;
  @NotNull
  @NotBlank
  private Boolean isNew;
  @NotNull
  @NotBlank
  private String location;
  @NotNull
  @NotBlank
  @Valid
  private CategoryDto category;
  private List<String> images;
}
