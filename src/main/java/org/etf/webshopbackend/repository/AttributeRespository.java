package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRespository extends JpaRepository<Attribute,Long> {

}
