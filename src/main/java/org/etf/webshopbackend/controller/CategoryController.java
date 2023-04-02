package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.model.request.CategoryRequest;
import org.etf.webshopbackend.model.response.CategoryResponse;
import org.etf.webshopbackend.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public List<CategoryResponse> findAll() {
    return categoryService.findAll();
  }

  @GetMapping("nested")
  public List<Category> findAllNested() {
    return categoryService.findAllNested();
  }

  @PostMapping
  public Category inserCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
    return categoryService.insertCategory(categoryRequest);
  }
}
