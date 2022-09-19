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
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.service.ScooterService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/management")
@Api(tags = "Контроллер самокатов")
public class ScooterController extends BaseController {
  private final ScooterService scooterService;

  @DeleteMapping("/scooter/{id}")
  @ApiOperation(value = "Удаление самоката по идентификатору")
  public void deleteScooter(@PathVariable(name = "id") Long id) {
    scooterService.deleteScooter(id);
  }

  @PostMapping("/scooter")
  @ApiOperation(value = "Добавление нового самоката")
  public Scooter createNewScooter(@Valid @RequestBody NewScooterDTO newScooter) {
    return scooterService.addNewScooter(newScooter);
  }

  @PutMapping("/scooter/{id}")
  @ApiOperation(value = "Обновление самоката")
  public Scooter updateScooter(
      @PathVariable(name = "id") Long id, @Valid @RequestBody NewScooterDTO updatedScooter) {
    return scooterService.updateScooter(id, updatedScooter);
  }

  @GetMapping("/scooter/{id}")
  @ApiOperation(value = "Получение самоката")
  public Scooter findScooter(@PathVariable(name = "id") Long id) {
    return scooterService.findScooter(id);
  }

  @GetMapping("/scooters")
  @ApiOperation(value = "Получение всех самокатов")
  public List<Scooter> findAllScooters() {
    return scooterService.findAllScooters();
  }
}
