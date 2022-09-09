package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.skqwk.kicksharingservice.enumeration.TariffType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.temporal.ChronoUnit;

/**
 * Сущность для представления тарифа пользователю.
 *
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffDTO {
  @NotBlank(message = "Tariff name must not be empty")
  private String name;

  @NotBlank(message = "Tariff description must not be empty")
  private String description;

  private Double settlementCost;
  private ChronoUnit settlementFor;
  private Double discount = 0.0;
  private Double activationCost;
  private Double tariffCost;
  private Integer durationInHours;

  @NotNull(message = "TariffType must not be null")
  private TariffType type;
}
