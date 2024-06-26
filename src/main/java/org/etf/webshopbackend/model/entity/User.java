package org.etf.webshopbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(max = 255)
  @NotNull
  private String username;

  @JsonIgnore
  @Size(max = 255)
  @NotNull
  private String password;

  @Size(max = 255)
  @NotNull
  private String firstName;

  @Size(max = 255)
  @NotNull
  private String lastName;

  @Size(max = 255)
  @NotNull
  private String email;

  @Size(max = 255)
  @NotNull
  private String city;

  private String avatarPath;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean isDeleted = false;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean isActive = false;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  private Role role;

}
