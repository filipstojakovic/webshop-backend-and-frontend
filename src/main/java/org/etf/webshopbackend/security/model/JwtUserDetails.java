package org.etf.webshopbackend.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.etf.webshopbackend.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserDetails implements UserDetails {

  private Long id;
  private String username;
  private String password;
  private Boolean isActive;
  private Boolean isDeleted;
  private String role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }

  public static JwtUserDetails create(User user) {
    return JwtUserDetails.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .isActive(user.getIsActive())
        .isDeleted(user.getIsDeleted())
        .role(user.getRole().getName())
        .build();
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
