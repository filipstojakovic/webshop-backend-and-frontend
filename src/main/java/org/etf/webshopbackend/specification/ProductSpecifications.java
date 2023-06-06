package org.etf.webshopbackend.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.etf.webshopbackend.model.entity.Attribute;
import org.etf.webshopbackend.model.entity.Category_;
import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.ProductHasAttribute;
import org.etf.webshopbackend.model.entity.Product_;
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

  public static Specification<Product> byCategoryName(String categoryName) { // should use categoryId
    if (Objects.isNull(categoryName)) {
      return emptySpecification();
    }
    return (root, query, criteriaBuilder) -> {
      // Join the category entity
      root.join(Product_.CATEGORY);

      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get(Product_.CATEGORY).get(Category_.NAME)), "%" + categoryName.toLowerCase() + "%"
      );
    };
  }

  public static Specification<Product> hasAttributeWithValue(String attributeName, String attributeValue) {
    return (root, query, criteriaBuilder) -> {
      Join<Product, ProductHasAttribute> join = root.join("productHasAttributes");
      Join<ProductHasAttribute, Attribute> attributeJoin = join.join("attribute");

      Predicate attributePredicate = criteriaBuilder.equal(attributeJoin.get("name"), attributeName);
      Predicate valuePredicate = criteriaBuilder.equal(join.get("value"), attributeValue);

      return criteriaBuilder.and(attributePredicate, valuePredicate);
    };
  }

}
