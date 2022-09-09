package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Сущность для возврата ближайших точек аренд пользователю с указанием дистанции от пользователя.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentPointClosestDTO {
  private Long id;
  private Double latitude;
  private Double longitude;
  private Double distanceInKm;
}
