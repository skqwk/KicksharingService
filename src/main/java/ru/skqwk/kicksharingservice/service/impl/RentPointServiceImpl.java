package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skqwk.kicksharingservice.dto.IRentPoint;
import ru.skqwk.kicksharingservice.dto.RentPointClosestDTO;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.dto.UserRentPointDTO;
import ru.skqwk.kicksharingservice.dto.UserScooterDTO;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.exception.BadInputParametersException;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.repo.RentPointRepository;
import ru.skqwk.kicksharingservice.service.RentPointService;
import ru.skqwk.kicksharingservice.service.ScooterService;
import ru.skqwk.kicksharingservice.service.util.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Реализация сервиса для работы точками аренды самокатов. */
@Slf4j
@Service
@AllArgsConstructor
public class RentPointServiceImpl implements RentPointService {

  private final RentPointRepository rentPointRepository;
  private final ScooterService scooterService;

  /**
   * Находит ближайшие точки аренды по координатам пользователя
   *
   * @param latitude Широта.
   * @param longitude Долгота.
   */
  @Override
  public List<RentPointClosestDTO> findClosestRentPoints(double latitude, double longitude) {
    validate(latitude, longitude);

    return rentPointRepository
        .find10ClosestRentPointsOrderedByDistance(latitude, longitude)
        .stream()
        .map(this::mapIRentPointToClosestDTO)
        .collect(Collectors.toList());
  }

  /**
   * Находит точку аренды по id.
   *
   * @param id Идентификатор точки аренды.
   */
  @Override
  public RentPoint findRentPoint(Long id) {
    return rentPointRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("RentPoint with id: %s - not found", id)));
  }

  /**
   * Находит точку аренды по id для просмотра пользователем.
   *
   * @param id Идентификатор точки аренды.
   */
  @Override
  public UserRentPointDTO findUserRentPoint(Long id) {
    RentPoint rentPoint = findRentPoint(id);
    return mapRentPointToUserRentPointDTO(rentPoint);
  }

  /**
   * Добавляет новую точку аренды.
   *
   * @param newRentPointDTO Новая точка аренды.
   * @throws IllegalArgumentException Если переданы некорректные координаты.
   */
  @Override
  public RentPoint addNewRentPoint(RentPointDTO newRentPointDTO) {
    validate(newRentPointDTO.getLatitude(), newRentPointDTO.getLongitude());
    log.info(
        "Add new rent point at lat = {}, lng = {}",
        newRentPointDTO.getLatitude(),
        newRentPointDTO.getLongitude());
    RentPoint newRentPoint = mapRentPointDTOtoRentPoint(newRentPointDTO);
    newRentPoint.setScooters(new HashSet<>());
    return rentPointRepository.save(newRentPoint);
  }

  private void validate(double latitude, double longitude) {
    // The latitude must be a number between -90 and 90 and the longitude between -180 and 180
    try {
      Validator.requireInterval(latitude, -90, 90);
      Validator.requireInterval(longitude, -180, 180);
    } catch (IllegalArgumentException ex) {
      log.warn("Try add rent point with invalid lat = {}, lng = {}", latitude, longitude);
      throw new BadInputParametersException(
          "The latitude must be a number between -90 and 90 and the longitude between -180 and 180");
    }
  }

  /**
   * Удаляет точку аренды, самокаты прикрепленные к данной точке переводятся в состояние
   * NOT_ATTACHED.
   *
   * @param id Идентификатор точки аренды.
   */
  @Override
  @Transactional
  public void deleteRentPoint(Long id) {
    RentPoint rentPoint = findRentPoint(id);
    for (Scooter scooter : rentPoint.getScooters()) {
      scooter.setStatus(ScooterStatus.NOT_ATTACHED);
      scooterService.save(scooter);
    }
    rentPoint.setScooters(new HashSet<>());
    rentPointRepository.save(rentPoint);
    rentPointRepository.deleteById(id);
    log.info("Rent point with id = {} was deleted", id);
  }

  /**
   * Обновляет точку аренды.
   *
   * @param id Идентификатор точки аренды.
   * @param updatedRentPoint Обновленная точка аренды.
   * @throws IllegalArgumentException Если переданы некорректные координаты.
   */
  @Override
  public RentPoint updateRentPoint(Long id, RentPointDTO updatedRentPoint) {
    findRentPoint(id);
    validate(updatedRentPoint.getLatitude(), updatedRentPoint.getLongitude());
    RentPoint updated = mapRentPointDTOtoRentPoint(updatedRentPoint);
    updated.setId(id);
    return rentPointRepository.save(updated);
  }

  /**
   * Удаляет самокаты с точки аренды
   *
   * @param id Идентификатор точки аренды.
   * @param scooterIds Список идентификаторов самокатов, которые нужно убрать с точки аренды.
   */
  @Override
  @Transactional
  public RentPoint removeScootersFromRentPoint(Long id, List<Long> scooterIds) {
    RentPoint rentPoint = findRentPoint(id);

    // убедиться, что все переданные scooterId существуют на данной точке аренды
    Map<Long, Scooter> idToScooter =
        rentPoint.getScooters().stream()
            .collect(Collectors.toMap(Scooter::getId, scooter -> scooter));
    Set<Long> scooterIdsOnRentPoint = idToScooter.keySet();

    if (!scooterIdsOnRentPoint.containsAll(scooterIds)) {
      throw new IllegalStateException(
          "There are not such scooters on this rent point. Check and try again");
    }

    for (Long scooterId : scooterIds) {
      Scooter scooter = idToScooter.get(scooterId);
      scooter.setStatus(ScooterStatus.NOT_ATTACHED);
      scooterService.save(scooter);
      idToScooter.remove(scooterId);
    }

    Set<Scooter> updatedScooters = new HashSet<>(idToScooter.values());

    rentPoint.setScooters(updatedScooters);
    log.info("Remove {} scooters from rent point with id = {}", scooterIds.size(), id);
    return rentPointRepository.save(rentPoint);
  }

  /**
   * Добавляет самокаты на точку аренды
   *
   * @param id Идентификатор точки аренды.
   * @param scooterIds Список идентификаторов самокатов, которые нужно добавить на точку аренды.
   */
  @Override
  @Transactional
  public RentPoint addScootersToRentPoint(Long id, List<Long> scooterIds) {
    RentPoint rentPoint = findRentPoint(id);

    Set<Scooter> scooters = new HashSet<>();
    for (Long scooterId : scooterIds) {
      Scooter scooter = scooterService.findScooter(scooterId);
      if (!ScooterStatus.NOT_ATTACHED.equals(scooter.getStatus())) {
        log.warn(
            "Attempt to add scooter with id = {} and status = {} at rent point with id = {}",
            scooter.getId(),
            scooter.getStatus(),
            id);
        throw new IllegalStateException(
            String.format("Attempt to add taken scooter with id = %s", scooter.getId()));
      }
      scooter.setStatus(ScooterStatus.ON_POINT);
      scooters.add(scooter);
    }

    Set<Scooter> existedScooters = rentPoint.getScooters();
    existedScooters.addAll(scooters);
    rentPoint.setScooters(existedScooters);
    log.info("Add {} scooters to rent point with id = {}", scooterIds.size(), id);
    return rentPointRepository.save(rentPoint);
  }

  /**
   * Сохраняет точку аренды в базу данных.
   *
   * @param rentPoint Точка аренды самокатов.
   */
  @Override
  public void save(RentPoint rentPoint) {
    rentPointRepository.save(rentPoint);
  }

  private RentPointClosestDTO mapIRentPointToClosestDTO(IRentPoint rentPoint) {
    return RentPointClosestDTO.builder()
        .id(rentPoint.getId())
        .latitude(rentPoint.getLatitude())
        .distanceInKm(rentPoint.getDistanceInKm())
        .longitude(rentPoint.getLongitude())
        .build();
  }

  private RentPoint mapRentPointDTOtoRentPoint(RentPointDTO rentPointDTO) {
    return RentPoint.builder()
        .latitude(rentPointDTO.getLatitude())
        .longitude(rentPointDTO.getLongitude())
        .build();
  }

  private UserScooterDTO mapScooterToUserScooterDTO(Scooter scooter) {
    return UserScooterDTO.builder()
        .id(scooter.getId())
        .manufacturer(scooter.getModel().getManufacturer())
        .model(scooter.getModel().getName())
        .build();
  }

  private UserRentPointDTO mapRentPointToUserRentPointDTO(RentPoint rentPoint) {
    return UserRentPointDTO.builder()
        .id(rentPoint.getId())
        .latitude(rentPoint.getLatitude())
        .longitude(rentPoint.getLongitude())
        .scooters(
            rentPoint.getScooters().stream()
                .map(this::mapScooterToUserScooterDTO)
                .collect(Collectors.toSet()))
        .build();
  }
}
