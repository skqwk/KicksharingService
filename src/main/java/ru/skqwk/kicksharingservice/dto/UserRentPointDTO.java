package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/** Сущность для возвращения данных о точке аренды пользователю. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRentPointDTO {
  private Long id;

  private Double longitude;
  private Double latitude;

  private Set<UserScooterDTO> scooters = new HashSet<>();
}
