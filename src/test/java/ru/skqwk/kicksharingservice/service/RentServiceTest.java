package ru.skqwk.kicksharingservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skqwk.kicksharingservice.dto.UserRentDTO;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.model.UserAccount;
import ru.skqwk.kicksharingservice.repo.RentRepository;
import ru.skqwk.kicksharingservice.service.impl.RentServiceImpl;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RentServiceTest {

  private final TariffService tariffService = mock(TariffService.class);
  private final RentRepository rentRepository = mock(RentRepository.class);
  private final UserService userService = mock(UserService.class);
  private final RentPointService rentPointService = mock(RentPointService.class);
  private final ScooterService scooterService = mock(ScooterService.class);
  private final TariffCalculator settlementCalculator = mock(TariffCalculator.class);
  private final TariffCalculator subscriptionCalculator = mock(TariffCalculator.class);
  private RentService rentService;

  @BeforeEach
  public void setUp() {
    rentService =
        new RentServiceImpl(
            tariffService,
            rentRepository,
            scooterService,
            rentPointService,
            userService,
            Map.of(
                TariffType.SETTLEMENT_TARIFF, settlementCalculator,
                TariffType.SUBSCRIPTION_TARIFF, subscriptionCalculator));
  }

  @Test
  public void shouldFindRentHistoryByScooterId() {
    // Arrange
    when(scooterService.findScooter(1L)).thenReturn(any());

    // Act
    rentService.findRentHistoryByScooterId(1L);

    // Assert
    verify(scooterService).findScooter(1L);
    verify(rentRepository).findAllByScooter(any());
  }

  @Test
  public void shouldFindRentHistoryByUserId() {
    // Arrange
    when(userService.findUser(1L)).thenReturn(any());

    // Act
    rentService.findRentHistoryByUserIdForUserView(1L);

    // Assert
    verify(userService).findUser(1L);
    verify(rentRepository).findAllByUser(any());
  }

  @Test
  public void shouldThrowExceptionIfRentNotExisted() {
    // Arrange
    when(rentRepository.findById(1L)).thenReturn(Optional.empty());

    // Act and Assert
    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> rentService.completeRent(1L, 1L, 1L));
  }

  @Test
  public void shouldFinishRentAndCalculateCostIfRentExists() {
    // Arrange
    when(rentRepository.findByUserAndStatus(any(), eq(RentStatus.FINISHED)))
        .thenReturn(Optional.empty());
    when(rentRepository.findByUserAndId(any(), any()))
        .thenReturn(
            Optional.of(
                Rent.builder()
                    .startedIn(Instant.now())
                    .scooter(
                        Scooter.builder()
                            .id(1L)
                            .model(Model.builder().name("Model").build())
                            .build())
                    .tariff(Tariff.builder().type(TariffType.SETTLEMENT_TARIFF).build())
                    .build()));

    RentPoint returnedRentPoint = RentPoint.builder().scooters(new HashSet<>()).build();
    when(rentPointService.findRentPoint(any())).thenReturn(returnedRentPoint);
    when(userService.findUser(any())).thenReturn(UserAccount.builder().build());
    when(settlementCalculator.calculate(any())).thenReturn(20.0);

    // Act
    UserRentDTO rent = rentService.completeRent(1L, 1L, 1L);

    // Assert
    verify(settlementCalculator).calculate(any());
    verify(rentRepository).saveAndFlush(any(Rent.class));
    Assertions.assertEquals(20.0, rent.getCost());
    Assertions.assertNotNull(rent.getFinishedIn());
    Assertions.assertEquals(1, returnedRentPoint.getScooters().size());
  }

  @Test
  public void shouldStartAndCompleteRent() {
    // Arrange
    Scooter returnedScooter =
        Scooter.builder()
            .id(1L)
            .status(ScooterStatus.ON_POINT)
            .model(Model.builder().name("name").build())
            .build();
    RentPoint returnedRentPoint =
        RentPoint.builder().scooters(new HashSet<>(Set.of(returnedScooter))).build();

    when(rentPointService.findRentPoint(any())).thenReturn(returnedRentPoint);
    when(userService.findUser(any())).thenReturn(UserAccount.builder().build());
    when(scooterService.findScooter(any())).thenReturn(returnedScooter);
    when(tariffService.findTariff(any())).thenReturn(Tariff.builder().build());

    // Act
    UserRentDTO rent = rentService.startRent(1L, 1L, 1L, 1L);

    // Assert
    verify(userService).findUser(1L);
    verify(scooterService).findScooter(1L);
    verify(tariffService).findTariff(1L);
    Assertions.assertNotNull(rent.getStartedIn());
    Assertions.assertEquals(0, returnedRentPoint.getScooters().size());
    Assertions.assertEquals(ScooterStatus.IN_RENT, returnedScooter.getStatus());
  }
}
