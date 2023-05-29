package org.etf.webshopbackend.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

  @NotNull
  @NotBlank
  private String name;
  private Long parentCategoryId;
  @Valid
  List<AttributeRequest> attributes = new ArrayList<>();
}
