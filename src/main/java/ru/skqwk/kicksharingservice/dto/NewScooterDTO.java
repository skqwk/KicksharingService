package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Duration;

/** Сущность для добавления нового самоката через контроллер. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewScooterDTO {
  @NotNull Long modelId;
  Duration timeInUse;
}
