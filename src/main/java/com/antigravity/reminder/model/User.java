package com.antigravity.reminder.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** User entity representing a system user. Uses Lombok to reduce boilerplate. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private String timezone;
    private String smtpPassword;
    private LocalDateTime createdAt;
}
