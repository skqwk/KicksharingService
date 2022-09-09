package ru.skqwk.kicksharingservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.dto.AuthOkResponse;
import ru.skqwk.kicksharingservice.dto.UserCredentials;
import ru.skqwk.kicksharingservice.dto.UserRegisterRequest;
import ru.skqwk.kicksharingservice.model.UserAccount;
import ru.skqwk.kicksharingservice.service.AuthService;
import ru.skqwk.kicksharingservice.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Api(tags = "Контроллер авторизации и регистрации")
public class AuthController extends BaseController {

  private final UserService userService;
  private final AuthService authService;

  @PostMapping("/register")
  @ApiOperation(
      value = "Регистрация пользователя",
      notes = "Валидация пользовательских данных и возращение кода статуса регистрации")
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {
    userService.addNewUser(registerRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/auth")
  @ApiOperation(
      value = "Авторизация пользователя",
      notes = "Проверка пользовательских данных и возращение токена в случае успешной авторизации")
  public AuthOkResponse authUser(@RequestBody UserCredentials userCredentials) {
    String authToken = authService.authenticate(userCredentials);
    return AuthOkResponse.builder().authToken(authToken).build();
  }

  @DeleteMapping("/account")
  @ApiOperation(value = "Удаление аккаунта текущего пользователя")
  public void deleteAccount(@AuthenticationPrincipal UserAccount userAccount) {
    userService.deleteAccount(userAccount.getId());
  }
}
