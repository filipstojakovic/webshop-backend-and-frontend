package org.etf.webshopbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributesResponse {
  private Long id; //attribute id
  private String name;
  private String value;
}
