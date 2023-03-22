package org.etf.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public List<User> findAll(){
    return userService.findAll();
  }
}
