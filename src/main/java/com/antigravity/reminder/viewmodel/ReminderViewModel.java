package com.antigravity.reminder.viewmodel;

import com.antigravity.reminder.model.Reminder;
import com.antigravity.reminder.service.ReminderService;
import java.time.LocalDateTime;
import java.util.List;

public class ReminderViewModel {
    private final ReminderService reminderService = new ReminderService();
    private List<Reminder> reminders;

    public List<Reminder> getReminders() {
        if (reminders == null) {
            refreshReminders();
        }
        return reminders;
    }

    public void refreshReminders() {
        this.reminders = reminderService.getMyReminders();
    }

    public void createReminder(
            String title, String description, LocalDateTime remindAt, int scheduleId) {
        reminderService.createReminder(title, description, remindAt, scheduleId);
        refreshReminders();
    }

    public void markAsCompleted(int reminderId) {
        reminderService.markAsCompleted(reminderId);
        refreshReminders();
    }

    public void deleteReminder(int reminderId) {
        reminderService.deleteReminder(reminderId);
        refreshReminders();
    }

    public List<Reminder> search(String query) {
        return reminderService.searchReminders(query);
    }

    public List<Reminder> filter(boolean completed) {
        return reminderService.getFilteredReminders(completed);
    }
}
