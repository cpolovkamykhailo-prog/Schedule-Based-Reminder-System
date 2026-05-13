-- Update schema to be more flexible for "Schedule-Based" reminders

-- 1. Add columns to reminders
ALTER TABLE reminders ADD COLUMN IF NOT EXISTS remind_at TIMESTAMP;
ALTER TABLE reminders ADD COLUMN IF NOT EXISTS is_completed BOOLEAN DEFAULT FALSE;

-- 2. Modify schedules to be independent of reminders (optional, but better)
-- For now, let's just add user_id to schedules and make reminder_id nullable
ALTER TABLE schedules ADD COLUMN IF NOT EXISTS user_id INTEGER REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE schedules ADD COLUMN IF NOT EXISTS name VARCHAR(100);
ALTER TABLE schedules ALTER COLUMN reminder_id DROP NOT NULL;
ALTER TABLE schedules DROP CONSTRAINT IF EXISTS schedules_reminder_id_key; -- Remove unique if it exists

-- 3. Add schedule_id to reminders
ALTER TABLE reminders ADD COLUMN IF NOT EXISTS schedule_id INTEGER REFERENCES schedules(id) ON DELETE SET NULL;
