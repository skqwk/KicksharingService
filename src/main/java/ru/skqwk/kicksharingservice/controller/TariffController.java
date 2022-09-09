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
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.dto.UserTariffDTO;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.service.TariffService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "Контроллер тарифов")
public class TariffController extends BaseController {

  private final TariffService tariffService;

  @GetMapping("/management/tariffs")
  @ApiOperation(value = "Получение всех тарифов для менеджера")
  public List<Tariff> findAllTariffs() {
    return tariffService.findAll();
  }

  @GetMapping("/management/tariff/{id}")
  @ApiOperation(value = "Получение тарифа по идентификатору для менеджера")
  public Tariff findTariff(@PathVariable("id") Long id) {
    return tariffService.findTariff(id);
  }

  @GetMapping("/user/tariff/{id}")
  @ApiOperation(value = "Получение тарифа по идентификатору для пользователя")
  public UserTariffDTO findUserTariff(@PathVariable("id") Long id) {
    return tariffService.findUserTariff(id);
  }

  @GetMapping("/user/tariffs")
  @ApiOperation(value = "Получение всех тарифов для пользователя")
  public List<UserTariffDTO> findAllUserTariffs() {
    return tariffService.findAllUserTariffs();
  }

  @DeleteMapping("/management/tariff/{id}")
  @ApiOperation(value = "Удаление тарифа")
  public void deleteTariff(@PathVariable("id") Long id) {
    tariffService.deleteTariff(id);
  }

  @PostMapping("/management/tariff")
  @ApiOperation(value = "Добавление нового тарифа")
  public Tariff addNewTariff(@Valid @RequestBody TariffDTO newTariff) {
    return tariffService.addNewTariff(newTariff);
  }

  @PutMapping("/management/tariff/{id}")
  @ApiOperation(value = "Обновление тарифа")
  public Tariff updateTariff(@PathVariable("id") Long id, @RequestBody TariffDTO updatedTariff) {
    return tariffService.updateTariff(id, updatedTariff);
  }
}
