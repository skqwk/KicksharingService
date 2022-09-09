package ru.skqwk.kicksharingservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Контроллер доступности сервиса")
public class PingController {

  @GetMapping("/ping")
  @ApiOperation(
      value = "Проверяет доступность сервиса",
      notes = " Возвращает \"pong\", если сервис доступен")
  public String ping() {
    return "pong";
  }
}
