package ru.skqwk.kicksharingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Сущность для возвращения токена после успешной авторизации. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthOkResponse {
  @JsonProperty("authToken")
  private String authToken;
}
