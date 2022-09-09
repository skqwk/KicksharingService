package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.model.Scooter;

import java.util.List;

public interface ScooterService {
  Scooter addNewScooter(NewScooterDTO newScooter);

  Scooter updateScooter(Long id, NewScooterDTO updatedScooter);

  void deleteScooter(Long id);

  Scooter findScooter(Long id);

  List<Scooter> findAllScooters();

  Scooter save(Scooter scooter);
}
