package ru.skqwk.kicksharingservice.service.calculator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.service.TariffCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Сервис для создания бина с калькуляторами тарифов. */
@Slf4j
@Configuration
@AllArgsConstructor
public class TariffCalculatorConfigurer {

  private final List<TariffCalculator> calculators;

  @Bean
  public Map<TariffType, TariffCalculator> tariffToCalculator() {
    Map<TariffType, TariffCalculator> tariffToCalculator = new HashMap<>();
    for (TariffCalculator calculator : calculators) {
      tariffToCalculator.put(calculator.getType(), calculator);
    }
    log.info("TariffCalculatorConfigurer find {} calculators", calculators.size());
    return tariffToCalculator;
  }
}
