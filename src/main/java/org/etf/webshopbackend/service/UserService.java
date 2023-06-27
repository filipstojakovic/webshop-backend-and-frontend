package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.Role;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.enums.RoleEnum;
import org.etf.webshopbackend.model.mapper.UserMapper;
import org.etf.webshopbackend.model.request.UserPasswordRequest;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.request.UserUpdateRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.repository.RoleRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public List<UserResponse> findAll() {
    List<User> users = userRepository.findAll();
    return userMapper.toResponses(users, UserResponse.class);
  }

  public UserResponse findById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(User.class, id));
    return userMapper.toResponse(user, UserResponse.class);
  }

  public UserResponse insert(UserRequest userRequest) {
    Optional<User> userByUsername = userRepository.findByUsername(userRequest.getUsername());
    if (userByUsername.isPresent()) {
      throw new BadRequestException("Username already exists!");
    }
    User user = storeUser(userRequest);
    return userMapper.toResponse(user, UserResponse.class);
  }

  public UserResponse update(final Long id, UserUpdateRequest userRequest) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(User.class, id));

    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setCity(userRequest.getCity());

    return userMapper.toResponse(user, UserResponse.class);
  }

  private User storeUser(UserRequest userRequest) {
    if (userRequest.getPassword() == null) {
      throw new BadRequestException("User password is null!");
    }
    User user = userMapper.fromRequest(userRequest, User.class);
    user.setAvatarPath(userRequest.getAvatar());
    if (user.getRole() == null) {
      setDefaultUserRole(user);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.saveAndFlush(user);
  }

  public UserResponse changePassword(Long id, UserPasswordRequest userPasswordRequest) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    if (!passwordEncoder.matches(userPasswordRequest.getOldPassword(), user.getPassword())) {
      throw new BadRequestException("Old password does not match");
    }
    user.setPassword(passwordEncoder.encode(userPasswordRequest.getNewPassword()));
    return userMapper.toResponse(user, UserResponse.class);
  }

  private void setDefaultUserRole(User updatedUser) {
    Role userRole = roleRepository.findByName(RoleEnum.user.toString())
        .orElseThrow(() -> new NotFoundException("Role " + RoleEnum.user + " not found"));
    updatedUser.setRole(userRole);
  }

  public UserResponse delete(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(User.class, id));
    boolean isDeleted = !user.getIsDeleted();
    user.setIsDeleted(isDeleted);
    return userMapper.toResponse(user, UserResponse.class);
  }
}
