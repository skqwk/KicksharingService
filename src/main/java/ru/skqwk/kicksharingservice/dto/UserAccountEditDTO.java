package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/** Сущность для передачи данных для редактирования пользователя в контроллер. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountEditDTO {
  @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email is invalid")
  private String email;

  @Length(min = 8, max = 20, message = "Password length must be from 8 to 20")
  private String password;

  @Positive(message = "Age must be positive")
  private Integer age;
}
