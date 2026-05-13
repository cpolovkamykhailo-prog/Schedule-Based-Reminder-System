package com.antigravity.reminder.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Schedule entity representing a recurring pattern for reminders. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Integer id;
    private Integer reminderId;
    private Integer userId;
    private String name;
    private String frequency; // ONCE, DAILY, WEEKLY, MONTHLY
    private Integer interval;
    private LocalDateTime startTime;
    private LocalDateTime nextRun;
    private LocalDateTime lastRun;
}
