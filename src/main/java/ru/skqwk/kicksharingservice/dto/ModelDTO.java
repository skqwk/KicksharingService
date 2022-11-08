package ru.skqwk.kicksharingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/** Сущность для добавления модели самокатов через контроллер. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelDTO {
  @NotBlank(message = "Model name should be not blank")
  private String name;

  @NotBlank(message = "Model manufacturer should be not blank")
  private String manufacturer;
}
