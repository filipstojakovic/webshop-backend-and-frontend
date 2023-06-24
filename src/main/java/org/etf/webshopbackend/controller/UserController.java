package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.UnAuthorizedException;
import org.etf.webshopbackend.model.enums.RoleEnum;
import org.etf.webshopbackend.model.request.UserPasswordRequest;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.request.UserUpdateRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.security.model.JwtUserDetails;
import org.etf.webshopbackend.service.FileService;
import org.etf.webshopbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

  private final UserService userService;
  private final FileService fileService;

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll() {
    List<UserResponse> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
    UserResponse user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<UserResponse> insert(@Valid @RequestBody UserRequest userRequest) {
    UserResponse user = userService.insert(userRequest);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  // TODO: user can only update his own profile (cant update username)

  @PutMapping("{id}")
  public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                             @Valid @RequestBody UserUpdateRequest userRequest,
                                             @AuthenticationPrincipal JwtUserDetails jwtUser
  ) {
    if (!id.equals(jwtUser.getId()) && !RoleEnum.admin.name().equals(jwtUser.getRole())) {
      throw new UnAuthorizedException();
    }
    UserResponse user = userService.update(id, userRequest);
    return ResponseEntity.ok(user);
  }

  @PutMapping("{id}/change-password")
  public ResponseEntity<UserResponse> changePassword(@PathVariable Long id,
                                             @Valid @RequestBody UserPasswordRequest userPasswordRequest,
                                             @AuthenticationPrincipal JwtUserDetails jwtUser
  ) {
    if (!id.equals(jwtUser.getId()) && !RoleEnum.admin.name().equals(jwtUser.getRole())) {
      throw new UnAuthorizedException();
    }
    UserResponse user = userService.changePassword(id, userPasswordRequest);
    return ResponseEntity.ok(user);
  }

  @Async
  @GetMapping("{id}/image")
  public CompletableFuture<ResponseEntity<byte[]>> downloadUserProfileImage(@PathVariable Long id) {
    try {
      UserResponse user = userService.findById(id);
      if (user.getAvatarPath() != null) {
        var image = fileService.loadImageBytesFromPath(user.getAvatarPath());
        return CompletableFuture.completedFuture(ResponseEntity.ok(image));
      }
    } catch (Exception ex) {
      log.error("Unable to load avatar image");
    }
    return CompletableFuture.completedFuture(ResponseEntity.noContent().build());
  }
}
