package org.etf.webshopbackend.model.mapper;

import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.etf.webshopbackend.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class UserMapper extends GenericMapper<UserRequest, User, UserResponse> {

  private final FileService fileService;

  public UserMapper(final FileService fileService, final ModelMapper modelMapper) {
    super(modelMapper);
    this.fileService = fileService;
  }

  @Override
  public UserResponse toResponse(User user, Class<UserResponse> targetClass) {
    UserResponse userResponse = super.toResponse(user, UserResponse.class);

    String avatarImage = null;
    try {
      avatarImage = fileService.loadImageBase64FromPath(user.getAvatarPath());
    } catch (IOException e) {
      log.error("unable to load avatarImage from path: " + user.getAvatarPath());
    }
    userResponse.setAvatar(avatarImage);

    return userResponse;
  }
}
