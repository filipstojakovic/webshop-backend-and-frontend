package org.etf.webshopbackend.specification;

import org.etf.webshopbackend.model.entity.Attribute_;
import org.etf.webshopbackend.model.entity.Category_;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.ProductHasAttribute_;
import org.etf.webshopbackend.model.entity.Product_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ProductSpecifications {

  public static Specification<Product> productByName(String name) {
    return (root, query, criteriaBuilder) -> {
      if (Objects.isNull(name)) {
        return criteriaBuilder.and(); // it's like empty criteria
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get(Product_.NAME)),
          "%" + name.toLowerCase() + "%"
      );
    };
  }

  public static Specification<Product> notPurchased() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(Product_.IS_PURCHASED));
  }

  public static Specification<Product> byCategoryName(String categoryName) { // should use categoryId
    return (root, query, criteriaBuilder) -> {
      // Join the category entity
      root.join(Product_.CATEGORY);

      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get(Product_.CATEGORY).get(Category_.NAME)), "%" + categoryName.toLowerCase() + "%"
      );
    };
  }

  public static Specification<Product> byAttributeNameAndValue(String attributeName, String attributeValue) {
    Specification<Product> byAttributeNameSpec = byAttributeName(attributeName);
    Specification<Product> byAttributeValueSpec = byAttributeValue(attributeValue);

    return byAttributeNameSpec.and(byAttributeValueSpec);
  }

  private static Specification<Product> byAttributeName(String attributeName) {
    return (root, query, criteriaBuilder) -> {
      // Join the productHasAttributes and attributes entities
      root.join(Product_.PRODUCT_HAS_ATTRIBUTES);
      root.join(Product_.PRODUCT_HAS_ATTRIBUTES).join(ProductHasAttribute_.ATTRIBUTE);

      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get(Product_.PRODUCT_HAS_ATTRIBUTES).get(ProductHasAttribute_.ATTRIBUTE)
              .get(Attribute_.NAME)), "%" + attributeName.toLowerCase() + "%"
      );
    };
  }

  private static Specification<Product> byAttributeValue(String attributeValue) {
    return (root, query, criteriaBuilder) -> {
      // Join the productHasAttributes entity
      root.join(Product_.PRODUCT_HAS_ATTRIBUTES);

      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get(Product_.PRODUCT_HAS_ATTRIBUTES).get(ProductHasAttribute_.VALUE)), "%" + attributeValue.toLowerCase() + "%"
      );
    };
  }

}
