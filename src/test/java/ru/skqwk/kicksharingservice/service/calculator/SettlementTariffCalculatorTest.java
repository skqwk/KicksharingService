package ru.skqwk.kicksharingservice.service.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.service.TariffCalculator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

class SettlementTariffCalculatorTest {

  private final TariffCalculator tariffCalculator = new SettlementTariffCalculator();

  // Для расчетных тарифов цена считается по формуле:
  // (длительность_аренды * стоимость_аренды + стоимость_активации) * (1.0 - скидка)
  @Test
  void calculate() {
    // Arrange
    double settlementCost = 10.0;
    double activationCost = 20.0;
    double discount = 0.1;
    int durationInHours = 2;

    Tariff tariff =
        Tariff.builder()
            .type(TariffType.SETTLEMENT_TARIFF)
            .settlementFor(ChronoUnit.HOURS)
            .settlementCost(settlementCost)
            .discount(discount)
            .activationCost(activationCost)
            .build();

    Rent rent =
        Rent.builder()
            .tariff(tariff)
            .startedAt(Instant.now())
            .finishedAt(Instant.now().plus(durationInHours, ChronoUnit.HOURS))
            .build();
    // Act
    double calculatedCost = tariffCalculator.calculate(rent);

    // Assert
    double expectedCost = (durationInHours * settlementCost + activationCost) * (1 - discount);
    Assertions.assertEquals(expectedCost, calculatedCost);
  }
}
