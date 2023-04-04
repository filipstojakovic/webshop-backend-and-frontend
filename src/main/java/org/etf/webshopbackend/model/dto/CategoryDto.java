package org.etf.webshopbackend.model.dto;

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

  private Long id;
  private String name;
  private List<AttributeNameValueRequest> attributes;
}
