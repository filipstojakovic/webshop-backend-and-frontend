package org.etf.webshopbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String city;
  private String avatarPath;
  private Boolean isDeleted;
  private String roleName;
}
