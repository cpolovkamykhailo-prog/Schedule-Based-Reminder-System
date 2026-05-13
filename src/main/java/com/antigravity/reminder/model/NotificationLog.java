package com.antigravity.reminder.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** NotificationLog entity representing a record of a sent notification. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {
    private Integer id;
    private Integer reminderId;
    private LocalDateTime sentAt;
    private String status;
    private String channelType;
    private String errorMessage;
}
