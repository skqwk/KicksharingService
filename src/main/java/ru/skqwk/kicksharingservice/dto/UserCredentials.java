package ru.skqwk.kicksharingservice.dto;

import lombok.Builder;
import lombok.Getter;

/** Сущность для реквизитов пользователя для авторизации. */
@Getter
@Builder
public class UserCredentials {
  private String email;
  private String password;
}
