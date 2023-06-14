package org.etf.webshopbackend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

  @NotNull
  @NotBlank
  private String firstName;
  @NotNull
  @NotBlank
  private String lastName;
  @NotNull
  @NotBlank
  private String email;
  @NotNull
  @NotBlank
  private String city;
}
