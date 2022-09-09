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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.dto.RentPointClosestDTO;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.dto.ScooterWrapper;
import ru.skqwk.kicksharingservice.dto.UserRentPointDTO;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.service.RentPointService;

import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "Контроллер точек аренды")
public class RentPointController extends BaseController {

  private final RentPointService rentPointService;

  @GetMapping("/user/rent-points")
  @ApiOperation(value = "Возвращение 10 ближайших к пользователю точек аренды")
  public List<RentPointClosestDTO> findClosestRentPoints(
      @RequestParam(name = "lng") Double longitude, @RequestParam(name = "lat") Double latitude) {
    return rentPointService.findClosestRentPoints(latitude, longitude);
  }

  @GetMapping("/user/rent-point/{id}")
  @ApiOperation(value = "Получение детальной информации по точке аренды для пользователя")
  public UserRentPointDTO findUserRentPoint(@PathVariable(name = "id") Long id) {
    return rentPointService.findUserRentPoint(id);
  }

  @GetMapping("/management/rent-point/{id}")
  @ApiOperation(value = "Получение детальной информации по точке аренды для менеджера")
  public RentPoint findRentPoint(@PathVariable(name = "id") Long id) {
    return rentPointService.findRentPoint(id);
  }

  @DeleteMapping("/management/rent-point/{id}")
  @ApiOperation(
      value = "Удаление точки аренды",
      notes =
          "При удалении точки аренды, все самокаты на данной точке не удаляются, а переводятся в состояние \"NOT_ATTACHED\"")
  public void deleteRentPoint(@PathVariable(name = "id") Long id) {
    rentPointService.deleteRentPoint(id);
  }

  @PostMapping("/management/rent-point")
  @ApiOperation(value = "Добавление новой точки аренды")
  public RentPoint createNewRentPoint(@RequestBody RentPointDTO newRentPoint) {
    return rentPointService.addNewRentPoint(newRentPoint);
  }

  @PutMapping("/management/rent-point/{id}")
  @ApiOperation(value = "Обновление существующей точки аренды по идентификатору")
  public RentPoint updateRentPoint(
      @PathVariable(name = "id") Long id, @RequestBody RentPointDTO updatedRentPoint) {
    return rentPointService.updateRentPoint(id, updatedRentPoint);
  }

  @PutMapping("/management/rent-point/{id}/add-scooters")
  @ApiOperation(value = "Добавление самокатов на точку аренды")
  public RentPoint addScootersToRentPoint(
      @PathVariable(name = "id") Long id, @RequestBody ScooterWrapper scooterWrapper) {
    return rentPointService.addScootersToRentPoint(id, scooterWrapper.getScooters());
  }

  @PutMapping("/management/rent-point/{id}/remove-scooters")
  @ApiOperation(value = "Удаление самокатов с точки аренды")
  public RentPoint removeScootersFromRentPoint(
      @PathVariable(name = "id") Long id, @RequestBody ScooterWrapper scooterWrapper) {
    return rentPointService.removeScootersFromRentPoint(id, scooterWrapper.getScooters());
  }
}
