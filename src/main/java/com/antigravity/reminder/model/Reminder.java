package com.antigravity.reminder.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Reminder entity representing a user's reminder task. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    private Integer id;
    private Integer userId;
    private Integer scheduleId;
    private String title;
    private String description;
    private LocalDateTime remindAt;
    private boolean isCompleted;
    private boolean isEnabled;
    private LocalDateTime createdAt;
}
