package org.etf.webshopbackend.model.entity.compositekey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode // Equals and HashCode methods are MANDATORY
@Embeddable
public class ProductAttributeId implements Serializable {
  @JsonIgnore // TODO: we can comment this, depends on what we want as response
  private Long productId;

  private Long attributeId;
}
