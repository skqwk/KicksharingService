package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;

import java.time.Instant;

/** Сущность для возвращения данных об аренде пользователю. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRentDTO {
  private Long id;
  private String scooterModel;
  private String tariffName;
  private Instant startedAt;
  private Instant finishedAt;
  private Double cost;
  private RentStatus status;
}
