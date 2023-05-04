package org.etf.webshopbackend.model.entity.specification;

import org.etf.webshopbackend.model.entity.Product;
import org.etf.webshopbackend.model.entity.Product_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ProductSpecifications {

  public static Specification<Product> productHasNameWith(String name) {
    return (root, query, criteriaBuilder) -> {
      if (Objects.isNull(name)) {
        return criteriaBuilder.and(); // it's like empty criteria
      }
      return criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    };
  }
}
