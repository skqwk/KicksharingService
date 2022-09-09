package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skqwk.kicksharingservice.dto.ManagerRentDTO;
import ru.skqwk.kicksharingservice.dto.UserAccountDTO;
import ru.skqwk.kicksharingservice.dto.UserRentDTO;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.model.UserAccount;
import ru.skqwk.kicksharingservice.repo.RentRepository;
import ru.skqwk.kicksharingservice.service.RentPointService;
import ru.skqwk.kicksharingservice.service.RentService;
import ru.skqwk.kicksharingservice.service.ScooterService;
import ru.skqwk.kicksharingservice.service.TariffCalculator;
import ru.skqwk.kicksharingservice.service.TariffService;
import ru.skqwk.kicksharingservice.service.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса старта, завершения аренды, получения истории аренд по конкретному самокату.
 */
@Slf4j
@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService {

  private final TariffService tariffService;
  private final RentRepository rentRepository;
  private final ScooterService scooterService;
  private final RentPointService rentPointService;
  private final UserService userService;
  private final Map<TariffType, TariffCalculator> tariffToCalculator;

  /**
   * Находит список аренд по идентификатору самоката.
   *
   * @param scooterId Идентификатор самоката.
   */
  @Override
  public List<ManagerRentDTO> findRentHistoryByScooterId(Long scooterId) {
    log.info("Request on history view by scooter = {}", scooterId);
    Scooter scooter = scooterService.findScooter(scooterId);
    return rentRepository.findAllByScooter(scooter).stream()
        .map(this::mapRentToManagerRentDTO)
        .collect(Collectors.toList());
  }

  private List<Rent> findRentHistoryByUserId(Long userId) {
    UserAccount user = userService.findUser(userId);
    return rentRepository.findAllByUser(user);
  }

  /**
   * Начинает аренду самоката.
   *
   * @param scooterId Идентификатор самоката.
   * @param rentPointId Идентификатор точки аренды.
   * @param userId Идентификатор пользователя.
   * @param tariffId Идентификатор тарифа.
   * @throws IllegalStateException Если не завершена предыдущая аренда. <br>
   *     Если указанный самокат занят. <br>
   *     Если самоката нет на точке аренды.
   */
  @Override
  public UserRentDTO startRent(Long userId, Long rentPointId, Long scooterId, Long tariffId) {
    UserAccount user = userService.findUser(userId);
    Optional<Rent> rentO = rentRepository.findByUserAndStatus(user, RentStatus.STARTED);
    if (rentO.isPresent()) {
      throw new IllegalStateException("Can't start rent. You must finished previous rent");
    }

    Scooter scooter = scooterService.findScooter(scooterId);
    if (!ScooterStatus.ON_POINT.equals(scooter.getStatus())) {
      throw new IllegalStateException(
          String.format("Can't start rent. Scooter with id = %s not available", scooterId));
    }

    RentPoint rentPoint = rentPointService.findRentPoint(rentPointId);

    if (!rentPoint.getScooters().contains(scooter)) {
      throw new IllegalStateException(
          String.format(
              "Can't start rent. Scooter with id = %s not available in this rent point",
              scooterId));
    }

    rentPoint.getScooters().remove(scooter);
    rentPointService.save(rentPoint);

    scooter.setStatus(ScooterStatus.IN_RENT);
    scooterService.save(scooter);

    Tariff tariff = tariffService.findTariff(tariffId);

    Rent newRent =
        Rent.builder()
            .status(RentStatus.STARTED)
            .tariff(tariff)
            .scooter(scooter)
            .user(user)
            .startedIn(Instant.now())
            .build();

    rentRepository.saveAndFlush(newRent);
    log.info(
        "Start new rent ({}) with userId = {}, rentPointId = {}, scooterId = {}, tariffId = {}",
        newRent.getId(),
        userId,
        rentPointId,
        scooterId,
        tariffId);
    return mapRentToUserRentDTO(newRent);
  }

  private Rent findRent(Long rentId) {
    return rentRepository
        .findById(rentId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(String.format("Rent with id %s not found", rentId)));
  }

  /**
   * Завершает аренду самоката.
   *
   * @param userId Идентификатор пользователя.
   * @param rentId Идентификатор аренды.
   * @param rentPointId Идентификатор точки аренды.
   * @throws IllegalStateException Если статус аренды уже завершен.
   */
  @Override
  @Transactional
  public UserRentDTO completeRent(Long userId, Long rentId, Long rentPointId) {
    UserAccount user = userService.findUser(userId);
    RentPoint rentPoint = rentPointService.findRentPoint(rentPointId);
    Rent rent =
        rentRepository
            .findByUserAndId(user, rentId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Such rent for your account not found"));

    if (RentStatus.FINISHED.equals(rent.getStatus())) {
      throw new IllegalStateException(
          String.format("Try complete rent with id = %s, but rent is already finished", rentId));
    }
    rent.setFinishedIn(Instant.now());

    Scooter scooter = rent.getScooter();
    scooter.setStatus(ScooterStatus.ON_POINT);
    scooter.setTimeInUse(Duration.between(rent.getStartedIn(), rent.getFinishedIn()));
    scooterService.save(scooter);

    rentPoint.getScooters().add(scooter);
    rentPointService.save(rentPoint);

    TariffCalculator calculator = tariffToCalculator.get(rent.getTariff().getType());
    Double cost = calculator.calculate(rent);
    rent.setCost(cost);
    rent.setStatus(RentStatus.FINISHED);
    rentRepository.saveAndFlush(rent);
    log.info(
        "Complete rent ({}) with userId = {} at rentPointId = {}, scooterId = {}, tariffId = {}",
        rent.getId(),
        userId,
        rentPointId,
        rent.getScooter().getId(),
        rent.getTariff().getId());
    return mapRentToUserRentDTO(rent);
  }

  /**
   * Находит список аренд для пользовательского просмотра.
   *
   * @param userId Идентификатор пользователя.
   */
  @Override
  public List<UserRentDTO> findRentHistoryByUserIdForUserView(Long userId) {
    log.info("Request on history view for user = {}", userId);
    return findRentHistoryByUserId(userId).stream()
        .map(this::mapRentToUserRentDTO)
        .collect(Collectors.toList());
  }

  private UserRentDTO mapRentToUserRentDTO(Rent rent) {
    return UserRentDTO.builder()
        .id(rent.getId())
        .scooterModel(rent.getScooter() != null ? rent.getScooter().getModel().getName() : null)
        .tariffName(rent.getTariff() != null ? rent.getTariff().getName() : null)
        .cost(rent.getCost())
        .startedIn(rent.getStartedIn())
        .finishedIn(rent.getFinishedIn())
        .status(rent.getStatus())
        .build();
  }

  private ManagerRentDTO mapRentToManagerRentDTO(Rent rent) {
    UserAccount user = rent.getUser();
    return ManagerRentDTO.builder()
        .user(
            user != null
                ? UserAccountDTO.builder().email(user.getEmail()).age(user.getAge()).build()
                : null)
        .cost(rent.getCost())
        .startedIn(rent.getStartedIn())
        .finishedIn(rent.getFinishedIn())
        .status(rent.getStatus())
        .scooter(rent.getScooter())
        .tariff(rent.getTariff())
        .build();
  }
}
