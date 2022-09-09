package ru.skqwk.kicksharingservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.service.ModelService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "Контроллер моделей самокатов")
@RequestMapping("/management")
public class ModelController extends BaseController {

  private final ModelService modelService;

  @GetMapping("/models")
  @ApiOperation(value = "Получение всех моделей самокатов")
  List<Model> findAllModels() {
    return modelService.findAllModels();
  }

  @GetMapping("/model/{id}")
  @ApiOperation(value = "Получение модели самоката по идентификатору")
  Model findModelById(@PathVariable("id") Long id) {
    return modelService.findModelById(id);
  }

  @PostMapping("/model")
  @ApiOperation(value = "Добавление модели самоката")
  Model addNewModel(@Valid @RequestBody ModelDTO modelDTO) {
    return modelService.addNewModel(modelDTO);
  }

  @PutMapping("/model/{id}")
  @ApiOperation(value = "Обновление существующей модели самоката по идентификатору")
  Model updateModel(@PathVariable("id") Long id, @Valid @RequestBody ModelDTO modelDTO) {
    return modelService.updateModel(id, modelDTO);
  }

  @DeleteMapping("/model/{id}")
  @ApiOperation(value = "Удаление модели самоката по идентификатору")
  void deleteModel(@PathVariable("id") Long id) {
    modelService.deleteModel(id);
  }
}
