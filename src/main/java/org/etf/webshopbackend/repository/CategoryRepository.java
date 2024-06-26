package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query(value = "SELECT * FROM category c WHERE c.parent_category_id is null",
      nativeQuery = true)
  List<Category> findRootCategory();

  List<Category> findByParentCategory(Category category);

}
