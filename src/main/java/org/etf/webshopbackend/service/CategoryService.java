package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<Category> findAll() {
    List<Category> rootCategory = categoryRepository.findRootCategory();
    for (var category : rootCategory) {
      List<Category> subCategory = categoryRepository.findByCategory(category);
      category.setSubCategories(subCategory);
    }
    return rootCategory;
  }
}
