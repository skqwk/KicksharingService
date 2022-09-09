package ru.skqwk.kicksharingservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.dto.ManagerRentDTO;
import ru.skqwk.kicksharingservice.dto.UserRentDTO;
import ru.skqwk.kicksharingservice.model.UserAccount;
import ru.skqwk.kicksharingservice.service.RentService;

import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "Контроллер аренды")
public class RentController extends BaseController {

  private final RentService rentService;

  @PostMapping("/user/rent/start")
  @ApiOperation(
      value = "Начало аренды",
      notes =
          "Начинает аренду самоката с указанной точки аренды, по переданному тарифу с текущим авторизованным пользователем")
  public UserRentDTO startRent(
      @AuthenticationPrincipal UserAccount userAccount,
      @RequestParam("rentPointId") Long rentPointId,
      @RequestParam("scooterId") Long scooterId,
      @RequestParam("tariffId") Long tariffId) {
    return rentService.startRent(userAccount.getId(), rentPointId, scooterId, tariffId);
  }

  @PostMapping("/user/rent/complete")
  @ApiOperation(
      value = "Завершение аренды",
      notes =
          "Завершает аренду самоката на указанной точке аренды с текущим авторизованным пользователем")
  public UserRentDTO completeRent(
      @AuthenticationPrincipal UserAccount userAccount,
      @RequestParam("rentId") Long rentId,
      @RequestParam("rentPointId") Long rentPointId) {
    return rentService.completeRent(userAccount.getId(), rentId, rentPointId);
  }

  @GetMapping("/management/rents/scooter/{scooterId}")
  @ApiOperation(value = "Получение истории аренд по идентификатору самоката")
  public List<ManagerRentDTO> findRentHistoryByScooterId(
      @PathVariable("scooterId") Long scooterId) {
    return rentService.findRentHistoryByScooterId(scooterId);
  }
}
