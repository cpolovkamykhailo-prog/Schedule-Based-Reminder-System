package com.antigravity.reminder.service;

import com.antigravity.reminder.dao.ScheduleDao;
import com.antigravity.reminder.model.Schedule;
import com.antigravity.reminder.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleService {
    private final ScheduleDao scheduleDao = new ScheduleDao();

    public void createSchedule(
            String name, String frequency, int interval, LocalDateTime startTime) {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) throw new RuntimeException("User not authenticated");

        Schedule schedule =
                Schedule.builder()
                        .userId(currentUser.getId())
                        .name(name)
                        .frequency(frequency)
                        .interval(interval)
                        .startTime(startTime)
                        .nextRun(startTime)
                        .build();

        scheduleDao.save(schedule);
    }

    public List<Schedule> getMySchedules() {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) return List.of();

        return scheduleDao.getAll().stream()
                .filter(s -> s.getUserId() == currentUser.getId())
                .collect(Collectors.toList());
    }

    public void deleteSchedule(int scheduleId) {
        scheduleDao.delete(scheduleId);
    }
}
