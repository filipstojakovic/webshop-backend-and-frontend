package org.etf.webshopbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.etf.webshopbackend.model.dto.AttributeDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

  private Long id;
  private String name;
  private List<AttributeDto> attributes;
}
