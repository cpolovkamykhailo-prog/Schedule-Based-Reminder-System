package com.antigravity.reminder.viewmodel;

import com.antigravity.reminder.dto.LoginDto;
import com.antigravity.reminder.dto.UserRegistrationDto;
import com.antigravity.reminder.service.AuthService;

public class AuthViewModel {
    private final AuthService authService = new AuthService();

    public boolean login(String username, String password) {
        return authService.login(new LoginDto(username, password));
    }

    public void register(String username, String email, String password, String timezone) {
        UserRegistrationDto dto =
                UserRegistrationDto.builder()
                        .username(username)
                        .email(email)
                        .password(password)
                        .timezone(timezone)
                        .build();
        authService.register(dto);
    }

    public boolean isAuthenticated() {
        return authService.isAuthenticated();
    }

    public void logout() {
        authService.logout();
    }

    public String getCurrentUsername() {
        return AuthService.getCurrentUser() != null
                ? AuthService.getCurrentUser().getUsername()
                : "";
    }
}
