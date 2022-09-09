package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Сущность для возвращения ошибок. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private int code;
  private String message;
}
