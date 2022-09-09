package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Сущность для возвращения данных о самокате пользователю. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserScooterDTO {
  private Long id;
  private String model;
  private String manufacturer;
}
