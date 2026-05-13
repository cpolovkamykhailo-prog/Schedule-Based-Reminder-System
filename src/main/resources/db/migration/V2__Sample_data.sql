-- =============================================================================
-- DML Script: Initial Data for Schedule-Based Reminder System
-- =============================================================================

-- Insert Users
INSERT INTO users (username, email, password_hash, timezone) VALUES
('john_doe', 'john@example.com', 'hashed_pass_123', 'Europe/Kyiv'),
('jane_smith', 'jane@work.com', 'hashed_pass_456', 'America/New_York');

-- Insert Tags
INSERT INTO tags (name, color) VALUES
('Work', '#e74c3c'),
('Personal', '#2ecc71'),
('Urgent', '#f1c40f'),
('Health', '#9b59b6');

-- Insert Reminders
-- For john_doe (ID: 1)
INSERT INTO reminders (user_id, title, description) VALUES
(1, 'Daily Standup', 'Morning meeting with the team'),
(1, 'Gym Session', 'Don''t forget leg day'),
(1, 'Pay Rent', 'Transfer money to the landlord');

-- For jane_smith (ID: 2)
INSERT INTO reminders (user_id, title, description) VALUES
(2, 'Project Deadline', 'Final submission for the database project'),
(2, 'Vitamin D', 'Take daily supplement');

-- Insert Schedules
-- ID 1 (Daily Standup) - DAILY
INSERT INTO schedules (reminder_id, frequency, start_time, next_run) VALUES
(1, 'DAILY', '2026-05-08 09:00:00', '2026-05-08 09:00:00');

-- ID 2 (Gym Session) - WEEKLY
INSERT INTO schedules (reminder_id, frequency, "interval", start_time, next_run) VALUES
(2, 'WEEKLY', 1, '2026-05-10 18:00:00', '2026-05-10 18:00:00');

-- ID 4 (Project Deadline) - ONCE
INSERT INTO schedules (reminder_id, frequency, start_time, next_run) VALUES
(4, 'ONCE', '2026-05-15 23:59:00', '2026-05-15 23:59:00');

-- Associate Tags (Many-to-Many)
INSERT INTO reminder_tags (reminder_id, tag_id) VALUES
(1, 1), -- Standup -> Work
(1, 3), -- Standup -> Urgent
(2, 2), -- Gym -> Personal
(2, 4), -- Gym -> Health
(4, 1), -- Project -> Work
(4, 3); -- Project -> Urgent

-- Insert Notification Logs
INSERT INTO notification_logs (reminder_id, sent_at, status, channel_type) VALUES
(1, '2026-05-07 09:00:05', 'SUCCESS', 'PUSH'),
(2, '2026-05-03 18:00:10', 'SUCCESS', 'EMAIL'),
(5, '2026-05-07 08:00:00', 'FAILED', 'SMS'); -- Example failure
