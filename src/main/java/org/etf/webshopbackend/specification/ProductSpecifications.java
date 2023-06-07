package org.etf.webshopbackend.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.etf.webshopbackend.model.entity.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ProductSpecifications {

  public static Specification<Product> productByName(String name) {
    if (Objects.isNull(name)) {
      return emptySpecification();
    }

    return (root, query, criteriaBuilder) -> criteriaBuilder.like(
        criteriaBuilder.lower(root.get(Product_.NAME)),
        "%" + name.toLowerCase() + "%"
    );
  }

  public static Specification<Product> emptySpecification() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.and(); // it's like empty criteria
  }

  public static Specification<Product> notPurchased() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(Product_.IS_PURCHASED));
  }

  public static Specification<Product> byCategoryId(Long categoryId) {
    if (Objects.isNull(categoryId)) {
      return emptySpecification();
    }
    return (root, query, criteriaBuilder) -> {
      // Join the category entity
      root.join(Product_.CATEGORY);

      return criteriaBuilder.equal(root.get(Product_.CATEGORY).get(Category_.ID), categoryId);
    };
  }

  public static Specification<Product> hasAttributeWithValue(String attributeName, String attributeValue) {
    return (root, query, criteriaBuilder) -> {
      Join<Product, ProductHasAttribute> join = root.join(Product_.PRODUCT_HAS_ATTRIBUTES);
      Join<ProductHasAttribute, Attribute> attributeJoin = join.join(ProductHasAttribute_.ATTRIBUTE);

      Predicate attributePredicate = criteriaBuilder.equal(attributeJoin.get(Attribute_.NAME), attributeName);
      Predicate valuePredicate = criteriaBuilder.like(
          criteriaBuilder.lower(join.get(ProductHasAttribute_.VALUE)), "%" + attributeValue.toLowerCase() + "%"
      );

      return criteriaBuilder.and(attributePredicate, valuePredicate);
    };
  }

}
