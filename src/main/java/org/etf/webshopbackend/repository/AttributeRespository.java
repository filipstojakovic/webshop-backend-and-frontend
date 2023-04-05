package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeRespository extends JpaRepository<Attribute, Long> {

  Optional<Attribute> findAttributeByCategory_IdAndName(Long categoryId, String name);
}
