package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

//   @Query(value = "SELECT * FROM category c WHERE c.parent_category_id is null",
//       nativeQuery = true)
//   List<Category> findRootCategory();
//
//   List<Category> findByCategory(Category category);

}
