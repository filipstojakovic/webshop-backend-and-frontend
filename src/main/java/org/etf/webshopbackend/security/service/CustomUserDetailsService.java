package org.etf.webshopbackend.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.repository.UserRepository;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
    return JwtUserDetails.create(user);
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id : " + id));
    return JwtUserDetails.create(user);
  }
}


