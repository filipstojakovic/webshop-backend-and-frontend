package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  public List<User> findAll(){
    return userRepository.findAll();
  }
}
