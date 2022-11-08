package ru.skqwk.kicksharingservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.exception.BadInputParametersException;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.repo.TariffRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static ru.skqwk.kicksharingservice.enumeration.TariffType.SETTLEMENT_TARIFF;
import static ru.skqwk.kicksharingservice.enumeration.TariffType.SUBSCRIPTION_TARIFF;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class TariffServiceTest {

  @Autowired private TariffService tariffService;

  @Autowired private TariffRepository tariffRepository;

  private static Stream<Arguments> provideInvalidTariffs() {
    return Stream.of(
        Arguments.of(
            TariffDTO.builder()
                .type(SUBSCRIPTION_TARIFF)
                .settlementFor(ChronoUnit.HOURS)
                .settlementCost(-10.0)
                .durationInHours(-10)
                .tariffCost(-10.0)
                .activationCost(-10.0)
                .build()),
        Arguments.of(
            TariffDTO.builder()
                .type(SUBSCRIPTION_TARIFF)
                .settlementFor(ChronoUnit.HOURS)
                .settlementCost(10.0)
                .durationInHours(100)
                .tariffCost(10.0)
                .discount(-1.0)
                .build()),
        Arguments.of(TariffDTO.builder().type(SETTLEMENT_TARIFF).build()),
        Arguments.of(
            TariffDTO.builder()
                .type(SUBSCRIPTION_TARIFF)
                .settlementFor(ChronoUnit.HOURS)
                .durationInHours(100)
                .tariffCost(10.0)
                .discount(2.0)
                .build()));
  }

  @AfterEach
  public void tearDown() {
    tariffRepository.deleteAll();
  }

  @Test
  public void shouldFindAllTariffs() {
    // Arrange
    tariffRepository.save(createValidSettlementTariff());

    tariffRepository.save(createValidSettlementTariff());

    // Act
    List<Tariff> founded = tariffService.findAll();

    // Assert
    Assertions.assertEquals(2, founded.size());
  }

  @Test
  public void shouldFindAnyTariff() {
    // Arrange
    Tariff saved1 = tariffRepository.save(createValidSettlementTariff());

    Tariff saved2 = tariffRepository.save(createValidSubscriptionTariff());

    // Act
    Tariff tariff1 = tariffService.findTariff(saved1.getId());
    Tariff tariff2 = tariffService.findTariff(saved2.getId());

    // Assert
    Assertions.assertEquals(SETTLEMENT_TARIFF, tariff1.getType());
    Assertions.assertEquals(SUBSCRIPTION_TARIFF, tariff2.getType());
  }

  @Test
  public void shouldDeleteAnyTariff() {
    // Arrange
    Tariff saved1 = tariffRepository.save(createValidSettlementTariff());

    Tariff saved2 = tariffRepository.save(createValidSettlementTariff());

    // Act
    tariffService.deleteTariff(saved1.getId());
    tariffService.deleteTariff(saved2.getId());

    // Assert
    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> tariffService.findTariff(saved1.getId()));
    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> tariffService.findTariff(saved2.getId()));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidTariffs")
  public void shouldThrowIfTariffInvalid(TariffDTO tariffDTO) {
    Assertions.assertThrows(
        BadInputParametersException.class, () -> tariffService.addNewTariff(tariffDTO));
  }

  private Tariff createValidSettlementTariff() {
    return Tariff.builder()
        .name("Тариф")
        .description("Описание")
        .type(SETTLEMENT_TARIFF)
        .settlementFor(ChronoUnit.HOURS)
        .activationCost(30.0)
        .settlementCost(200.0)
        .discount(0.1)
        .build();
  }

  private Tariff createValidSubscriptionTariff() {
    return Tariff.builder()
        .name("Тариф")
        .description("Описание")
        .type(SUBSCRIPTION_TARIFF)
        .settlementFor(ChronoUnit.HOURS)
        .settlementCost(200.0)
        .tariffCost(100.0)
        .discount(0.1)
        .build();
  }
}
