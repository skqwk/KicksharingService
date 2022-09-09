package ru.skqwk.kicksharingservice.service.calculator;

import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.service.TariffCalculator;

import java.time.Duration;

/**
 * Сервис-калькулятор для подсчета стоимости аренды по расчетному тарифу.
 */
@Service
public class SettlementTariffCalculator implements TariffCalculator {

  @Override
  public Double calculate(Rent rent) {
    Tariff settlementTariff = rent.getTariff();
    Duration absoluteDuration = Duration.between(rent.getStartedIn(), rent.getFinishedIn());
    Duration settlementForDuration = settlementTariff.getSettlementFor().getDuration();

    long relativeDuration = absoluteDuration.dividedBy(settlementForDuration);

    double discount = 1.0 - settlementTariff.getDiscount();
    double cost =
        (relativeDuration * settlementTariff.getSettlementCost()
                + settlementTariff.getActivationCost())
            * discount;

    return cost;
  }

  @Override
  public TariffType getType() {
    return TariffType.SETTLEMENT_TARIFF;
  }
}
