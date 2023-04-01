package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Attribute;
import org.etf.webshopbackend.model.entity.Category;
import org.etf.webshopbackend.repository.AttributeRespository;
import org.etf.webshopbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AttributeService {

  private final AttributeRespository attributeRespository;
  private final CategoryRepository categoryRepository;

  public List<Attribute> insertCategoryAttributes(Long categoryId, List<Attribute> attributesRequest) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException(Category.class, categoryId));
    return insertCategoryAttributes(category, attributesRequest);
  }

  public List<Attribute> insertCategoryAttributes(Category category, List<Attribute> attributesRequest) {
    setAttributesCategory(category, attributesRequest);
    return attributeRespository.saveAllAndFlush(attributesRequest);
  }

  private void setAttributesCategory(Category category, List<Attribute> attributesRequest) {
    attributesRequest.forEach(attribute -> attribute.setCategory(category));
  }
}
