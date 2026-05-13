package com.antigravity.reminder.viewmodel;

import com.antigravity.reminder.model.Schedule;
import com.antigravity.reminder.service.ScheduleService;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleViewModel {
    private final ScheduleService scheduleService = new ScheduleService();
    private List<Schedule> schedules;

    public List<Schedule> getSchedules() {
        if (schedules == null) {
            refreshSchedules();
        }
        return schedules;
    }

    public void refreshSchedules() {
        this.schedules = scheduleService.getMySchedules();
    }

    public void createSchedule(
            String name, String frequency, int interval, LocalDateTime startTime) {
        scheduleService.createSchedule(name, frequency, interval, startTime);
        refreshSchedules();
    }

    public void deleteSchedule(int scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        refreshSchedules();
    }
}
