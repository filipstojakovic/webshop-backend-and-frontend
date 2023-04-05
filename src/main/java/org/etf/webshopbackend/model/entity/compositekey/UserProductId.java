package org.etf.webshopbackend.model.entity.compositekey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode // Equals and HashCode methods are MANDATORY
@Embeddable
public class UserProductId implements Serializable {

  @JsonIgnore // TODO: we can comment this, depends on what we want as response
  private Long userId;

  private Long productId;
}
