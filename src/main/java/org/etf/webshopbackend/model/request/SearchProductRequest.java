package org.etf.webshopbackend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequest {

  @NotNull
  private String nameSearch;
  @NotNull
  private String categorySearch;
  @NotNull
  private List<AttributeNameValueRequest> attributeNameValueRequests;
}
