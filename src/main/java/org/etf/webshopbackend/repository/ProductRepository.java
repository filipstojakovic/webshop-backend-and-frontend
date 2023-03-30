package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
