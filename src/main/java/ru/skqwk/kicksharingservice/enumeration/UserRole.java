package ru.skqwk.kicksharingservice.enumeration;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/** Роли пользователей в системе - обычный пользователь и менеджер. */
public enum UserRole {
  USER,
  MANAGER;

  public List<SimpleGrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", this.name())));
  }
}
