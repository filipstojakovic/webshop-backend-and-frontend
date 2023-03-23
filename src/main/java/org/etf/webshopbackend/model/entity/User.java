package org.etf.webshopbackend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(max = 45)
  @NotNull
  private String username;

  @Size(max = 45)
  @NotNull
  private String password;

  @Size(max = 45)
  @NotNull
  private String firstName;

  @Size(max = 45)
  @NotNull
  private String lastName;

  @Size(max = 45)
  @NotNull
  private String email;

  @Size(max = 45)
  @NotNull
  private String city;

  @Size(max = 255)
  @NotNull
  private String avatar;

  @NotNull
  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean isActive;

  @NotNull
  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean isDeleted;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.getName()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}