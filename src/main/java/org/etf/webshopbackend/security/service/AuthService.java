package org.etf.webshopbackend.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.BadCredentialsException;
import org.etf.webshopbackend.model.request.LoginRequest;
import org.etf.webshopbackend.model.request.RegisterRequest;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.security.token.TokenProvider;
import org.etf.webshopbackend.service.FileService;
import org.etf.webshopbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserService userService;
  private final TokenProvider tokenProvider;
  private final FileService fileService;

  private final AuthenticationManager authenticationManager;
  private final ModelMapper mapper;

  public String login(LoginRequest request) {
    String token;
    try {

      Authentication authenticate = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      token = tokenProvider.createToken(authenticate);

    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new BadCredentialsException("Wrong username or password...");
    }
    return token;
  }

  public UserResponse register(RegisterRequest registerRequest) {
    String avatarPathString = null;
    if (registerRequest.getAvatar() != null) {
      try {
        avatarPathString = fileService.saveImageToFilesystem(
            fileService.getAvatarDirPath(),
            registerRequest.getUsername(),
            registerRequest.getAvatar());
      } catch (IOException ex) {
        log.error("Unable to create image: " + registerRequest.getAvatar().getName());
      }
    }
    try {
      UserRequest user = mapper.map(registerRequest, UserRequest.class);
      user.setAvatarPath(avatarPathString);
      return userService.insert(user);

    } catch (Exception ex) {
      if (avatarPathString != null) {
        try {
          fileService.deleteFile(avatarPathString);
        } catch (IOException e) {
          log.error("Unable to delete file " + avatarPathString);
        }
      }
      throw ex;
    }

  }

}
