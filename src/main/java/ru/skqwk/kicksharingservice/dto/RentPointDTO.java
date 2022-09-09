package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Сущность для создания точки аренды через контроллер. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentPointDTO {
  private Double longitude;
  private Double latitude;
}
