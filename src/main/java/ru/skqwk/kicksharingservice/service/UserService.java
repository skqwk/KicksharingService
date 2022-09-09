package ru.skqwk.kicksharingservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.skqwk.kicksharingservice.dto.UserAccountEditDTO;
import ru.skqwk.kicksharingservice.message.UserRegisterRequest;
import ru.skqwk.kicksharingservice.model.UserAccount;

public interface UserService extends UserDetailsService {
  UserAccount findUser(Long id);

  void addNewUser(UserRegisterRequest registerRequest);

  void deleteAccount(Long id);

  void updateUserAccount(Long userId, UserAccountEditDTO updatedUserAccount);
}
