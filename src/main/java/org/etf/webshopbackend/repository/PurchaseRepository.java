package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  Optional<Purchase> findByProductId(Long productId);
}
