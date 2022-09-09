package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.ManagerRentDTO;
import ru.skqwk.kicksharingservice.dto.UserRentDTO;

import java.util.List;

public interface RentService {
  List<ManagerRentDTO> findRentHistoryByScooterId(Long scooterId);

  UserRentDTO startRent(Long userId, Long rentPointId, Long scooterId, Long tariffId);

  UserRentDTO completeRent(Long userId, Long rentId, Long rentPointId);

  List<UserRentDTO> findRentHistoryByUserIdForUserView(Long id);
}
