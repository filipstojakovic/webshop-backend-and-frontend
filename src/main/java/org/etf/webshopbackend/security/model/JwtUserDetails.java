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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserDetails implements UserDetails {

  private Long id;
  private String username;
  private String password;
  private String role;
  private Map<String, Object> attributes;

  public JwtUserDetails(final Long id, final String username, final String password, final String role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role));
  }

  public static JwtUserDetails create(User user) {

    return JwtUserDetails.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .role(user.getRole().getName())
        .build();
  }

  public static JwtUserDetails create(User user, Map<String, Object> attributes) {
    JwtUserDetails jwtUserDetails = JwtUserDetails.create(user);
    jwtUserDetails.setAttributes(attributes);
    return jwtUserDetails;
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
