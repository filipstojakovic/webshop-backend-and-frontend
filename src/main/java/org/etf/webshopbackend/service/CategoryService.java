package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.CategoryRequest;
import org.etf.webshopbackend.model.response.CategoryResponse;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

  private final AttributeService attributeService;
  private final CategoryRepository categoryRepository;
  private final GenericMapper<CategoryRequest, Category, Category> categoryMapper;
  private final GenericMapper<Category, Category, CategoryResponse> categoryResponseMapper;

  public List<Category> findAllNested() {
    List<Category> rootCategory = categoryRepository.findRootCategory();
    for (var category : rootCategory) {
      List<Category> subCategory = categoryRepository.findByParentCategory(category);
      category.setSubCategories(subCategory);
    }
    return rootCategory;
  }

  public Category insertCategory(CategoryRequest categoryRequest) {
    Long parentId = categoryRequest.getParentCategoryId();
    Category category = categoryMapper.fromRequest(categoryRequest, Category.class);
    category.setId(null);
    setCategoryParent(category, parentId);
    category = categoryRepository.saveAndFlush(category);

    attributeService.insertCategoryAttributes(category, category.getAttributes());

    return category;
  }

  private void setCategoryParent(Category category, Long parentId) {
    Category parentCategory = findCategoryByIdOrNull(parentId);
    category.setParentCategory(parentCategory);
    if (parentCategory != null && parentCategory.getParentCategory() != null) {
      parentCategory.getSubCategories().add(category);
    }
  }

  private Category findCategoryByIdOrNull(Long categoryId) {
    Category category = null;
    if (categoryId != null) {
      category = categoryRepository.findById(categoryId)
          .orElseThrow(() -> new NotFoundException(Category.class, categoryId));
    }
    return category;
  }

  public List<CategoryResponse> findAll() {
    List<Category> categories = categoryRepository.findAll();
    return categoryResponseMapper.toResponses(categories, CategoryResponse.class);
  }
}
