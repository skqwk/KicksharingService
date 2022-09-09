package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.RentPointClosestDTO;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.dto.UserRentPointDTO;
import ru.skqwk.kicksharingservice.model.RentPoint;

import java.util.List;

public interface RentPointService {
  List<RentPointClosestDTO> findClosestRentPoints(double latitude, double longitude);

  RentPoint findRentPoint(Long id);

  UserRentPointDTO findUserRentPoint(Long id);

  RentPoint addNewRentPoint(RentPointDTO newRentPoint);

  void deleteRentPoint(Long id);

  RentPoint updateRentPoint(Long id, RentPointDTO updatedRentPoint);

  RentPoint removeScootersFromRentPoint(Long id, List<Long> scooters);

  RentPoint addScootersToRentPoint(Long id, List<Long> scooters);

  void save(RentPoint rentPoint);
}
