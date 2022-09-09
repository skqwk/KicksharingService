package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Сущность для возвращения данных о тарифе пользователю. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTariffDTO {
  private Long id;
  private String name;
  private String description;
}
