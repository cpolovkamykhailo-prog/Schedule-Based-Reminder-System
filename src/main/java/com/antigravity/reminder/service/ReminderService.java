package com.antigravity.reminder.service;

import com.antigravity.reminder.dao.ReminderDao;
import com.antigravity.reminder.model.Reminder;
import com.antigravity.reminder.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderService {
    private final ReminderDao reminderDao = new ReminderDao();
    private final com.antigravity.reminder.dao.ScheduleDao scheduleDao =
            new com.antigravity.reminder.dao.ScheduleDao();
    private final com.antigravity.reminder.db.UnitOfWork<Reminder> uow =
            new com.antigravity.reminder.db.UnitOfWork<>(reminderDao);

    public void createReminder(
            String title, String description, LocalDateTime remindAt, int scheduleId) {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) throw new RuntimeException("User not authenticated");

        Integer finalScheduleId = null;
        if (scheduleId > 0) {
            if (scheduleDao.get(scheduleId).isEmpty()) {
                throw new RuntimeException("Розклад з ID " + scheduleId + " не знайдено.");
            }
            finalScheduleId = scheduleId;
        }

        Reminder reminder =
                Reminder.builder()
                        .userId(currentUser.getId())
                        .scheduleId(finalScheduleId)
                        .title(title)
                        .description(description)
                        .remindAt(remindAt)
                        .isCompleted(false)
                        .isEnabled(true)
                        .build();

        uow.registerNew(reminder);
        uow.commit();
    }

    public List<Reminder> getMyReminders() {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) return List.of();

        return reminderDao.getAll().stream()
                .filter(r -> r.getUserId() == currentUser.getId())
                .collect(Collectors.toList());
    }

    public void markAsCompleted(int reminderId) {
        reminderDao
                .get(reminderId)
                .ifPresent(
                        r -> {
                            r.setCompleted(true);
                            reminderDao.update(r);
                        });
    }

    public void deleteReminder(int reminderId) {
        reminderDao.delete(reminderId);
    }

    public List<Reminder> searchReminders(String query) {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) return List.of();

        String lowerQuery = query.toLowerCase();
        return getMyReminders().stream()
                .filter(
                        r ->
                                (r.getTitle() != null
                                                && r.getTitle().toLowerCase().contains(lowerQuery))
                                        || (r.getDescription() != null
                                                && r.getDescription()
                                                        .toLowerCase()
                                                        .contains(lowerQuery)))
                .collect(Collectors.toList());
    }

    public List<Reminder> getFilteredReminders(boolean completed) {
        return getMyReminders().stream()
                .filter(r -> r.isCompleted() == completed)
                .collect(Collectors.toList());
    }
}
