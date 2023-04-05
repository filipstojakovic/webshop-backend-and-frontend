package org.etf.webshopbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.etf.webshopbackend.model.entity.compositekey.ProductAttributeId;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="product_has_attribute")
public class ProductHasAttribute {

  @EmbeddedId
  ProductAttributeId id = new ProductAttributeId();

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("attributeId")
  @JoinColumn(name = "attribute_id")
  private Attribute attribute;

  //set this value somewhere
  private String value;

  public ProductHasAttribute(final Product product, final Attribute attribute) {
    this.product = product;
    this.attribute = attribute;
  }

  public ProductHasAttribute(final Product product, final Attribute attribute, final String value) {
    this.product = product;
    this.attribute = attribute;
    this.value = value;
  }
}
