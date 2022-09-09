package ru.skqwk.kicksharingservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.dto.UserAccountDTO;
import ru.skqwk.kicksharingservice.dto.UserAccountEditDTO;
import ru.skqwk.kicksharingservice.dto.UserRentDTO;
import ru.skqwk.kicksharingservice.model.UserAccount;
import ru.skqwk.kicksharingservice.service.RentService;
import ru.skqwk.kicksharingservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = "Контроллер профиля пользователя")
public class ProfileController extends BaseController {

  private final RentService rentService;
  private final UserService userService;

  @GetMapping("/profile")
  @ApiOperation(value = "Получение профиля текущего авторизованного пользователя")
  public UserAccountDTO getProfile(@AuthenticationPrincipal UserAccount userAccount) {
    return UserAccountDTO.builder()
            .age(userAccount.getAge())
            .email(userAccount.getEmail())
            .build();
  }

  @PutMapping("/profile/edit")
  @ApiOperation(value = "Редактирование профиля текущего авторизованного пользователя")
  public void editProfile(
      @AuthenticationPrincipal UserAccount userAccount,
      @Valid @RequestBody UserAccountEditDTO updatedUserAccount) {
    userService.updateUserAccount(userAccount.getId(), updatedUserAccount);
  }

  @GetMapping("/profile/history")
  @ApiOperation(value = "Получение истории аренды текущего авторизованного пользователя")
  public List<UserRentDTO> getHistoryRent(@AuthenticationPrincipal UserAccount userAccount) {
    return rentService.findRentHistoryByUserIdForUserView(userAccount.getId());
  }
}
