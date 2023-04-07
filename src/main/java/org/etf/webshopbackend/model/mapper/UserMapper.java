package org.etf.webshopbackend.model.mapper;

import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.request.UserRequest;
import org.etf.webshopbackend.model.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMapper extends GenericMapper<UserRequest, User, UserResponse> {


  public UserMapper(final ModelMapper modelMapper) {
    super(modelMapper);
  }

}
