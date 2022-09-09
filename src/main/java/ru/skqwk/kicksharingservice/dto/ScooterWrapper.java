package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Сущность-обертка для добавления/удаления самокатов по id. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScooterWrapper {
  private List<Long> scooters;
}
