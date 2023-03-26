package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {

  Optional<Pin> findByActivationPin(String pin);
}
