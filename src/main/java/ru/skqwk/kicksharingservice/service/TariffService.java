package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.dto.UserTariffDTO;
import ru.skqwk.kicksharingservice.model.Tariff;

import java.util.List;

public interface TariffService {
  void deleteTariff(Long id);

  Tariff findTariff(Long id);

  UserTariffDTO findUserTariff(Long id);

  List<Tariff> findAll();

  Tariff addNewTariff(TariffDTO newTarif);

  Tariff updateTariff(Long id, TariffDTO updatedTariff);

  List<UserTariffDTO> findAllUserTariffs();
}
