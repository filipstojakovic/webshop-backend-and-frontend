package org.etf.webshopbackend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotNull
  @NotBlank
  private String username;
  @NotNull
  @NotBlank
  private String password;
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
