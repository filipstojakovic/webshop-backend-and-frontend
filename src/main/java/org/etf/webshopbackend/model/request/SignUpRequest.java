package org.etf.webshopbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

//   @NotNull
//   @NotBlank
  private String username;
//   @NotNull
//   @NotBlank
  private String password;
//   @NotNull
//   @NotBlank
  private String firstName;
//   @NotNull
//   @NotBlank
  private String lastName;
//   @NotNull
//   @NotBlank
  private String email;
//   @NotNull
//   @NotBlank
  private String city;

  private MultipartFile avatar;
}
