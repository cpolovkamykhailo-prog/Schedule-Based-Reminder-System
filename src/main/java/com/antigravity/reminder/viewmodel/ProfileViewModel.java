package com.antigravity.reminder.viewmodel;

import com.antigravity.reminder.dao.UserDao;
import com.antigravity.reminder.model.User;
import com.antigravity.reminder.service.AuthService;

public class ProfileViewModel {
    private final UserDao userDao = new UserDao();

    public User getCurrentUser() {
        return AuthService.getCurrentUser();
    }

    public void updateProfile(String email, String smtpPassword) {
        User user = getCurrentUser();
        if (user != null) {
            user.setEmail(email);
            user.setSmtpPassword(smtpPassword);
            userDao.update(user);
        }
    }
}
