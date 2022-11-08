package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.dto.UserTariffDTO;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.repo.RentRepository;
import ru.skqwk.kicksharingservice.repo.TariffRepository;
import ru.skqwk.kicksharingservice.service.TariffService;
import ru.skqwk.kicksharingservice.service.TariffValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Реализация сервиса для работы с сервисами (создание, обновление, удаление, получение). */
@Slf4j
@Service
@AllArgsConstructor
public class TariffServiceImpl implements TariffService {

  private final TariffRepository tariffRepository;
  private final Map<TariffType, TariffValidator> tariffToValidator;
  private final RentRepository rentRepository;

  /**
   * Удаляет тариф.
   *
   * @param id Идентификатор тарифа.
   */
  @Override
  public void deleteTariff(Long id) {
    Tariff tariff = findTariff(id);
    if (rentRepository.findAllByTariffAndFinishedAt(tariff, null).size() == 0) {
      tariffRepository.deleteById(id);
      log.info("Tariff with id = {} is deleted", id);
    } else {
      log.warn("Can't delete tariff with id = {}, because it in use", id);
      throw new IllegalStateException(
          String.format("Can't delete tariff with id = %s, because it in use", id));
    }
  }

  /**
   * Находит тариф.
   *
   * @param id Идентификатор тарифа.
   */
  @Override
  public Tariff findTariff(Long id) {
    return tariffRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(String.format("Tariff with id: %s - not found", id)));
  }

  /**
   * Находит тариф для просмотра пользователем.
   *
   * @param id Идентификатор тарифа.
   */
  @Override
  public UserTariffDTO findUserTariff(Long id) {
    return mapTariffToUserTariffDTO(findTariff(id));
  }

  /** Находит все тарифы. */
  @Override
  public List<Tariff> findAll() {
    return tariffRepository.findAll();
  }

  /**
   * Добавляет новый тариф.
   *
   * @param newTarif Новый тариф.
   */
  @Override
  public Tariff addNewTariff(TariffDTO newTarif) {
    tariffToValidator.get(newTarif.getType()).validate(newTarif);
    Tariff saved = tariffRepository.save(mapTariffDTOtoTariff(newTarif));
    log.info("Add new tariff with name = {}", saved.getName());
    return saved;
  }

  /**
   * Обновляет существующий тариф.
   *
   * @param id Идентификатор тарифа.
   * @param updatedTariffDTO Обновленный тариф.
   */
  @Override
  public Tariff updateTariff(Long id, TariffDTO updatedTariffDTO) {
    tariffToValidator.get(updatedTariffDTO.getType()).validate(updatedTariffDTO);
    Tariff tariff = findTariff(id);
    if (rentRepository.findAllByTariffAndFinishedAt(tariff, null).size() == 0) {
      Tariff updatedTariff = mapTariffDTOtoTariff(updatedTariffDTO);
      updatedTariff.setId(id);
      return tariffRepository.save(updatedTariff);
    } else {
      log.warn("Can't update tariff with id = {}, because it in use", id);
      throw new IllegalStateException(
          String.format("Can't update tariff with id = %s, because it in use", id));
    }
  }

  /** Находит все тарифы для пользовательского просмотра. */
  @Override
  public List<UserTariffDTO> findAllUserTariffs() {
    return findAll().stream().map(this::mapTariffToUserTariffDTO).collect(Collectors.toList());
  }

  private Tariff mapTariffDTOtoTariff(TariffDTO tariffDTO) {
    return Tariff.builder()
        .name(tariffDTO.getName())
        .description(tariffDTO.getDescription())
        .settlementCost(tariffDTO.getSettlementCost())
        .settlementFor(tariffDTO.getSettlementFor())
        .discount(tariffDTO.getDiscount())
        .activationCost(tariffDTO.getActivationCost())
        .tariffCost(tariffDTO.getTariffCost())
        .durationInHours(tariffDTO.getDurationInHours())
        .type(tariffDTO.getType())
        .build();
  }

  private UserTariffDTO mapTariffToUserTariffDTO(Tariff tariff) {
    return UserTariffDTO.builder()
        .id(tariff.getId())
        .name(tariff.getName())
        .description(tariff.getDescription())
        .build();
  }
}
