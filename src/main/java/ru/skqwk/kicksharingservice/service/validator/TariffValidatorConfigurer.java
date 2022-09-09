package ru.skqwk.kicksharingservice.service.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.service.TariffValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для создания бина с валидаторами тарифов.
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class TariffValidatorConfigurer {
  private final List<TariffValidator> validators;

  @Bean
  public Map<TariffType, TariffValidator> tariffToValidator() {
    Map<TariffType, TariffValidator> tariffToValidator = new HashMap<>();
    for (TariffValidator validator : validators) {
      tariffToValidator.put(validator.getType(), validator);
    }
    log.info("TariffValidatorConfigurer find {} validators", validators.size());
    return tariffToValidator;
  }
}
