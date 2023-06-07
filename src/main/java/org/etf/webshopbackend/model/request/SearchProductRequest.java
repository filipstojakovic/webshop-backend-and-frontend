package org.etf.webshopbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequest {

  private String nameSearch;
  private Long categoryIdSearch;
  private List<AttributeNameValueRequest> attributeNameValueSearches;
}
