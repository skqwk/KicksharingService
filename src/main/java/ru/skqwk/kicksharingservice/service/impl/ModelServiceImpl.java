package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.repo.ModelRepository;
import ru.skqwk.kicksharingservice.repo.ScooterRepository;
import ru.skqwk.kicksharingservice.service.ModelService;

import java.util.List;

/**
 * Реализация сервиса для работы с моделями самокатов (создание, удаление, обновление, получение).
 */
@Slf4j
@Service
@AllArgsConstructor
public class ModelServiceImpl implements ModelService {
  private final ModelRepository modelRepository;
  private final ScooterRepository scooterRepository;

  /**
   * Находит модель по идентификатору.
   *
   * @param id Идентификатор модели.
   * @throws ResourceNotFoundException Если модель с таким идентификатором не найдена.
   */
  @Override
  public Model findModelById(Long id) {
    return modelRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(String.format("Model with id = %s not found", id)));
  }

  /**
   * Удаляет модель по идентификатору, удаляя вместе с ней все самокаты только в том случае если все
   * самокаты находятся в состоянии NOT_ATTACHED.
   *
   * @param id Идентификатор модели.
   * @throws IllegalStateException Если самокаты данной модели используются.
   */
  @Override
  @Transactional
  public void deleteModel(Long id) {
    Model model = findModelById(id);
    boolean allScootersAreNotInUse =
        scooterRepository.findAllByModel(model).stream()
            .allMatch(s -> ScooterStatus.NOT_ATTACHED.equals(s.getStatus()));
    if (allScootersAreNotInUse) {
      modelRepository.deleteById(id);
    } else {
      log.warn("Can't delete model with id = {}, because there are scooters in use", id);
      throw new IllegalStateException(
          String.format("Can't delete model with id = %s, because there are scooters in use", id));
    }
  }

  /**
   * Обновляет существующую модель.
   *
   * @param id Идентификатор модели.
   * @param updatedModelDTO Обновленная модель.
   */
  @Override
  public Model updateModel(Long id, ModelDTO updatedModelDTO) {
    findModelById(id);
    Model updatedModel = mapModelDTOtoModel(updatedModelDTO);
    updatedModel.setId(id);
    return modelRepository.save(updatedModel);
  }

  /**
   * Добавляет новую модель.
   *
   * @param newModel Новая модель самоката.
   */
  @Override
  public Model addNewModel(ModelDTO newModel) {
    Model saved = modelRepository.save(mapModelDTOtoModel(newModel));
    log.info("Add new model with name = {}", saved.getName());
    return saved;
  }

  /** Находит все модели самокатов. */
  @Override
  public List<Model> findAllModels() {
    return modelRepository.findAll();
  }

  private Model mapModelDTOtoModel(ModelDTO modelDTO) {
    return Model.builder()
        .name(modelDTO.getName())
        .manufacturer(modelDTO.getManufacturer())
        .build();
  }
}
