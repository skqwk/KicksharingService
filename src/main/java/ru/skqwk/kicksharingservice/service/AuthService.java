package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.message.UserCredentials;

public interface AuthService {
  String authenticate(UserCredentials userCredentials);
}
