package org.etf.webshopbackend.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.etf.webshopbackend.model.request.AttributeNameValueRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

  @NotNull
  private Long id;
  @NotNull
  @NotBlank
  private String name;
  @NotNull
  @NotBlank
  @Valid
  private List<AttributeNameValueRequest> attributes;
}
