package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.Attribute;
import org.etf.webshopbackend.service.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("categories/{id}/attributes")
public class AttributeController {

  private final AttributeService attributeService;

  @GetMapping
  public ResponseEntity<List<Attribute>> findAllCategoryAttributes(@PathVariable(name = "id") Long categoryId) {
    List<Attribute> attributes = attributeService.findAllCategoryAttributes(categoryId);
    return ResponseEntity.ok(attributes);
  }
}
