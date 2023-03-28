package org.etf.webshopbackend.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.BadCredentialsException;
import org.etf.webshopbackend.model.request.LoginRequest;
import org.etf.webshopbackend.model.request.SignUpRequest;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.security.token.TokenProvider;
import org.etf.webshopbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserService userService;
  private final ModelMapper mapper;
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

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

  public UserResponse register(SignUpRequest signUpRequest) {
    UserRequest user = mapper.map(signUpRequest, UserRequest.class);
    return userService.insert(user);
  }

  private static void saveToFilesystem(MultipartFile multipartFile) throws IOException {
    String dir = Files.createTempDirectory("tmpDir").toFile().getAbsolutePath();
    File file = new File(dir + File.pathSeparator + multipartFile.getName());

    try (OutputStream os = new FileOutputStream(file)) {
      os.write(multipartFile.getBytes());
    }
  }
}
