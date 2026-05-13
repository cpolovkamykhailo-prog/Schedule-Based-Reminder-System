-- =============================================================================
-- DDL Script: Schedule-Based Reminder System
-- Database System: PostgreSQL (Relational)
-- =============================================================================

-- 1. USERS Table
-- Entity Classification: Strong Entity (exists independently).
-- Normal Form: 3NF (all attributes depend only on the primary key, no transitive dependencies).
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    timezone VARCHAR(50) DEFAULT 'UTC',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. REMINDERS Table
-- Entity Classification: Weak Entity (depends on the 'users' entity for existence).
-- Normal Form: 3NF (transitive dependencies like 'username' are avoided by using user_id).
CREATE TABLE reminders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. SCHEDULES Table
-- Entity Classification: Weak Entity (depends on 'reminders').
-- Normal Form: 3NF (atomic values, no partial key dependencies, no transitive dependencies).
CREATE TABLE schedules (
    id SERIAL PRIMARY KEY,
    reminder_id INTEGER NOT NULL UNIQUE REFERENCES reminders(id) ON DELETE CASCADE,
    frequency VARCHAR(20) NOT NULL CHECK (frequency IN ('ONCE', 'DAILY', 'WEEKLY', 'MONTHLY')),
    interval INTEGER DEFAULT 1, -- e.g., every 2 weeks
    start_time TIMESTAMP NOT NULL,
    next_run TIMESTAMP,
    last_run TIMESTAMP
);

-- 4. TAGS Table
-- Entity Classification: Strong Entity (metadata that can exist without specific reminders initially).
-- Normal Form: 3NF.
CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(7) DEFAULT '#3498db' -- Hex color
);

-- 5. REMINDER_TAGS Table
-- Entity Classification: Associative Entity (resolves the Many-to-Many relationship between Reminders and Tags).
-- Normal Form: 3NF / BCNF.
CREATE TABLE reminder_tags (
    reminder_id INTEGER NOT NULL REFERENCES reminders(id) ON DELETE CASCADE,
    tag_id INTEGER NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (reminder_id, tag_id)
);

-- 6. NOTIFICATION_LOGS Table
-- Entity Classification: Weak Entity (depends on 'reminders', logs events triggered by them).
-- Normal Form: 3NF.
CREATE TABLE notification_logs (
    id SERIAL PRIMARY KEY,
    reminder_id INTEGER NOT NULL REFERENCES reminders(id) ON DELETE CASCADE,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL CHECK (status IN ('SUCCESS', 'FAILED', 'PENDING')),
    channel_type VARCHAR(20) NOT NULL CHECK (channel_type IN ('EMAIL', 'SMS', 'PUSH')),
    error_message TEXT
);
