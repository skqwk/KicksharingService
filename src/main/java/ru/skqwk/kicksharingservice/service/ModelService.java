package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.model.Model;

import java.util.List;

public interface ModelService {
  Model findModelById(Long modelId);

  void deleteModel(Long id);

  Model updateModel(Long id, ModelDTO updatedModel);

  Model addNewModel(ModelDTO updatedModel);

  List<Model> findAllModels();
}
