package org.etf.webshopbackend.repository;

import org.etf.webshopbackend.model.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {

  Optional<Pin> findByPinAndUser_Id(String pin, Long userId);

  void deleteAllByUser_Id(Long userId);

}
