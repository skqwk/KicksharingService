package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.service.RestorePasswordService;
import ru.skqwk.kicksharingservice.service.UserService;

@Service
@AllArgsConstructor
public class RestorePasswordServiceImpl implements RestorePasswordService {
    private final UserService userService;

    @Override
    public void restorePassword(String email) {
        userService.loadUserByUsername(email);


    }
}
