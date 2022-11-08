package ru.skqwk.kicksharingservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/** Сущность для реквизитов пользователя для регистрации. */
@Getter
@Builder
public class UserRegisterRequest {
  @Email(message = "Email is invalid")
  private String email;

  @Length(min = 8, max = 20, message = "Length from 8 to 20")
  private String password;

  private String managerToken;

  @NotNull(message = "Age is mandatory")
  @Positive(message = "Age must be positive")
  private Integer age;
}
