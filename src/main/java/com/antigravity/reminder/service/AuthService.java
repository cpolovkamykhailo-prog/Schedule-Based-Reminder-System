package com.antigravity.reminder.service;

import com.antigravity.reminder.dao.UserDao;
import com.antigravity.reminder.dto.LoginDto;
import com.antigravity.reminder.dto.UserRegistrationDto;
import com.antigravity.reminder.infrastructure.EmailService;
import com.antigravity.reminder.infrastructure.HashingService;
import com.antigravity.reminder.model.User;
import java.util.Optional;

public class AuthService {
    private final UserDao userDao = new UserDao();
    private final HashingService hashingService = new HashingService();
    private final EmailService emailService = new EmailService();

    private static User currentUser;

    public void register(UserRegistrationDto dto) {
        if (userDao.getByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userDao.getByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = hashingService.hash(dto.getPassword());
        User user =
                User.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .passwordHash(hashedPassword)
                        .timezone(dto.getTimezone() != null ? dto.getTimezone() : "UTC")
                        .build();

        userDao.save(user);
    }

    public boolean login(LoginDto dto) {
        Optional<User> userOpt = userDao.getByUsername(dto.getUsername());
        if (userOpt.isEmpty()) {
            userOpt = userDao.getByEmail(dto.getUsername());
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (hashingService.verify(dto.getPassword(), user.getPasswordHash())) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
