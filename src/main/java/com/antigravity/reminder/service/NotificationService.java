package com.antigravity.reminder.service;

import com.antigravity.reminder.dao.NotificationLogDao;
import com.antigravity.reminder.dao.ReminderDao;
import com.antigravity.reminder.dao.UserDao;
import com.antigravity.reminder.infrastructure.EmailService;
import com.antigravity.reminder.model.NotificationLog;
import com.antigravity.reminder.model.Reminder;
import com.antigravity.reminder.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class NotificationService {
    private final NotificationLogDao logDao = new NotificationLogDao();
    private final ReminderDao reminderDao = new ReminderDao();
    private final UserDao userDao = new UserDao();
    private final EmailService emailService = new EmailService();

    public void processDueNotifications() {
        try {
            List<Reminder> dueReminders = reminderDao.getPendingReminders();
            for (Reminder reminder : dueReminders) {
                userDao.get(reminder.getUserId())
                        .ifPresent(
                                user -> {
                                    try {
                                        sendReminderNotification(reminder, user);

                                        // Mark as completed so we don't send it again
                                        reminder.setCompleted(true);
                                        reminderDao.update(reminder);
                                    } catch (Exception e) {
                                        System.err.println(
                                                "Failed to send notification for reminder "
                                                        + reminder.getId()
                                                        + ": "
                                                        + e.getMessage());
                                    }
                                });
            }
        } catch (Exception e) {
            System.err.println("Error in background notification processing: " + e.getMessage());
        }
    }

    public void sendReminderNotification(Reminder reminder, User user) {
        try {
            // 1. Simulated Email (Console) - Now using real credentials from DB if present
            emailService.sendEmail(
                    user.getEmail(),
                    user.getSmtpPassword(),
                    user.getEmail(),
                    "Нагадування: " + reminder.getTitle(),
                    "Опис: " + reminder.getDescription());

            // 2. UI Popup Notification - Only show if it's the current user
            User currentUser = AuthService.getCurrentUser();
            if (currentUser != null && currentUser.getId().equals(user.getId())) {
                Platform.runLater(
                        () -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("🔔 Нове нагадування!");
                            alert.setHeaderText(reminder.getTitle());
                            alert.setContentText(reminder.getDescription());
                            alert.show();
                        });
            }

            logNotification(reminder.getId(), "SUCCESS", "EMAIL", null);
        } catch (Exception e) {
            logNotification(reminder.getId(), "FAILED", "EMAIL", e.getMessage());
            throw e;
        }
    }

    private void logNotification(
            int reminderId, String status, String channel, String errorMessage) {
        NotificationLog log =
                NotificationLog.builder()
                        .reminderId(reminderId)
                        .sentAt(LocalDateTime.now())
                        .status(status)
                        .channelType(channel)
                        .errorMessage(errorMessage)
                        .build();
        logDao.save(log);
    }

    public static class NotificationScheduler {
        private final NotificationService notificationService = new NotificationService();
        private final ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        public void start() {
            System.out.println("Notification Scheduler started...");
            scheduler.scheduleAtFixedRate(
                    notificationService::processDueNotifications, 0, 1, TimeUnit.MINUTES);
        }

        public void stop() {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }
}
