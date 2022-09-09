package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.model.Tariff;

import java.time.Instant;

/** Сущность возврата аренды для просмотра пользователем. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRentDTO {
  private UserAccountDTO user;
  private Scooter scooter;
  private Tariff tariff;
  private Instant startedIn;
  private Instant finishedIn;
  private Double cost;
  private RentStatus status;
}
