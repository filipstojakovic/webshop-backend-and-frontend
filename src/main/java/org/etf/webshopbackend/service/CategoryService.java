package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.CategoryRequest;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final GenericMapper<CategoryRequest, Category, Category> categoryMapper;

//   public List<Category> findAll() {
//     List<Category> rootCategory = categoryRepository.findRootCategory();
//     for (var category : rootCategory) {
//       List<Category> subCategory = categoryRepository.findByCategory(category);
//       category.setSubCategories(subCategory);
//     }
//     return rootCategory;
//   }

  public Category insertCategory(CategoryRequest categoryRequest) {
    Category category = categoryMapper.fromRequest(categoryRequest, Category.class);

    return categoryRepository.saveAndFlush(category);
  }
}
