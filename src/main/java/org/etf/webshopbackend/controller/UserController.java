package org.etf.webshopbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.service.FileService;
import org.etf.webshopbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
    UserResponse user = userService.update(id, userRequest);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
    UserResponse user = userService.delete(id);
    return ResponseEntity.ok(user);
  }

  @Async
  @GetMapping("{id}/image")
  public CompletableFuture<String> downloadUserProfileImage(@PathVariable Long id) {

    try {
      UserResponse user = userService.findById(id);
      CompletableFuture.completedFuture(fileService.loadImageBase64FromPath(user.getAvatarPath()));
    } catch (Exception ex) {
      log.error("Unable to download image");
    }
    return null;
  }
}
