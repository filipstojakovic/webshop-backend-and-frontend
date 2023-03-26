package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Role;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.enums.RoleEnum;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.repository.RoleRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

  private final GenericMapper<UserRequest, User, UserResponse> userMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  //TODO: filter deleted? read project
  public List<UserResponse> findAll() {
    List<User> users = userRepository.findAll();
    return userMapper.toResponses(users, UserResponse.class);
  }

  public UserResponse findById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    return userMapper.toResponse(user, UserResponse.class);
  }

  public UserResponse insert(UserRequest userRequest) {
    User user = storeUser(userRequest);
    return userMapper.toResponse(user, UserResponse.class);
  }

  public UserResponse update(final Long id, UserRequest userRequest) {
    if (!userRepository.existsById(id)) {
      throw new NotFoundException(User.class, id);
    }

    // TODO: check if admin update or user update his own profile
    // TODO: store image on system, get path
    User user = storeUser(userRequest);
    return userMapper.toResponse(user, UserResponse.class);
  }

  private User storeUser(UserRequest userRequest) {
    if (userRequest.getPassword() == null) {
      throw new BadRequestException("User password is null!");
    }
    User user = userMapper.fromRequest(userRequest, User.class);
    if (user.getRole() == null) {
      setDefaultUserRole(user);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.saveAndFlush(user);
  }

  private void setDefaultUserRole(User updatedUser) {
    Role userRole = roleRepository.findByName(RoleEnum.user.toString())
        .orElseThrow(() -> new NotFoundException("Role " + RoleEnum.user + " not found"));
    updatedUser.setRole(userRole);
  }

  public UserResponse delete(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    boolean isDeleted = !user.getIsDeleted();
    user.setIsDeleted(isDeleted);
    return userMapper.toResponse(user, UserResponse.class);
  }
}
