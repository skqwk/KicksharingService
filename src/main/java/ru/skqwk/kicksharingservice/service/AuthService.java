package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.UserCredentials;

public interface AuthService {
  String authenticate(UserCredentials userCredentials);
}
