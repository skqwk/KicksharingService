package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.repo.ScooterRepository;
import ru.skqwk.kicksharingservice.service.ModelService;
import ru.skqwk.kicksharingservice.service.ScooterService;

import java.time.Duration;
import java.util.List;

/** Реализация сервиса для работы с самокатами (создание, обновление, удаление, получение). */
@Service
@Slf4j
@AllArgsConstructor
public class ScooterServiceImpl implements ScooterService {

  private final ScooterRepository scooterRepository;
  private final ModelService modelService;

  /**
   * Добавляет новый самокат.
   *
   * @param newScooterDTO Данные нового самоката.
   */
  @Override
  public Scooter addNewScooter(NewScooterDTO newScooterDTO) {
    Scooter newScooter = mapNewScooterDTOtoScooter(newScooterDTO);
    newScooter.setStatus(ScooterStatus.NOT_ATTACHED);
    newScooter.setTimeInUse(Duration.ZERO);
    return scooterRepository.save(newScooter);
  }

  /**
   * Обновляет существующий самокат.
   *
   * @param id Идентификатор самоката.
   * @param updatedScooterDTO Обновленный самокат.
   */
  @Override
  public Scooter updateScooter(Long id, NewScooterDTO updatedScooterDTO) {
    findScooter(id);
    Scooter updatedScooter = mapNewScooterDTOtoScooter(updatedScooterDTO);
    updatedScooter.setId(id);
    return scooterRepository.save(updatedScooter);
  }

  private Scooter mapNewScooterDTOtoScooter(NewScooterDTO scooterDTO) {
    Model model = modelService.findModelById(scooterDTO.getModelId());
    Duration timeInUse = scooterDTO.getTimeInUse();
    return Scooter.builder().model(model).timeInUse(timeInUse).build();
  }

  /**
   * Удаляет самокат по идентификатору только если самокат в состоянии NOT_ATTACHED.
   *
   * @param id Идентификатор самоката.
   * @throws IllegalStateException Если удаляемый самокат используется.
   */
  @Override
  public void deleteScooter(Long id) {
    Scooter scooter = findScooter(id);
    if (ScooterStatus.NOT_ATTACHED.equals(scooter.getStatus())) {
      scooterRepository.deleteById(id);
    } else {
      log.warn(
          "Can't delete scooter with id = {}, because its state = {}", id, scooter.getStatus());
      throw new IllegalStateException(
          String.format(
              "Can't delete scooter with id = %s, because its state = %s",
              id, scooter.getStatus()));
    }
  }

  /**
   * Находит самокат по идентификатору.
   *
   * @param id Идентификатор самоката.
   */
  @Override
  public Scooter findScooter(Long id) {
    return scooterRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Scooter with id: %s - not found", id)));
  }

  /** Находит все самоката. */
  @Override
  public List<Scooter> findAllScooters() {
    return scooterRepository.findAll();
  }

  /**
   * Сохраняет самокат в базу данных.
   *
   * @param scooter Сохраняемый самокат.
   */
  @Override
  public Scooter save(Scooter scooter) {
    return scooterRepository.save(scooter);
  }
}
