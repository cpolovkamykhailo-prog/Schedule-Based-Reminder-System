package com.antigravity.reminder.dao;

import com.antigravity.reminder.db.CustomConnectionPool;
import com.antigravity.reminder.model.Reminder;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReminderDao implements Dao<Reminder> {
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public Optional<Reminder> get(int id) {
        String sql = "SELECT * FROM reminders WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToReminder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Reminder> getAll() {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminders";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                reminders.add(mapResultSetToReminder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return reminders;
    }

    @Override
    public void save(Reminder reminder) {
        String sql =
                "INSERT INTO reminders (user_id, schedule_id, title, description, remind_at,"
                        + " is_completed, is_enabled) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reminder.getUserId());
            if (reminder.getScheduleId() != null && reminder.getScheduleId() > 0) {
                ps.setInt(2, reminder.getScheduleId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, reminder.getTitle());
            ps.setString(4, reminder.getDescription());
            if (reminder.getRemindAt() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(reminder.getRemindAt()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setBoolean(6, reminder.isCompleted());
            ps.setBoolean(7, reminder.isEnabled());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void update(Reminder reminder) {
        String sql =
                "UPDATE reminders SET title = ?, description = ?, remind_at = ?, is_completed = ?,"
                        + " schedule_id = ?, is_enabled = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, reminder.getTitle());
            ps.setString(2, reminder.getDescription());
            if (reminder.getRemindAt() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(reminder.getRemindAt()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            ps.setBoolean(4, reminder.isCompleted());
            if (reminder.getScheduleId() != null && reminder.getScheduleId() > 0) {
                ps.setInt(5, reminder.getScheduleId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setBoolean(6, reminder.isEnabled());
            ps.setInt(7, reminder.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    public List<Reminder> getPendingReminders() {
        List<Reminder> reminders = new ArrayList<>();
        String sql =
                "SELECT * FROM reminders WHERE remind_at <= ? AND is_completed = false AND"
                        + " is_enabled = true";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reminders.add(mapResultSetToReminder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return reminders;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM reminders WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    private Reminder mapResultSetToReminder(ResultSet rs) throws SQLException {
        return Reminder.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .scheduleId(rs.getObject("schedule_id", Integer.class))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .remindAt(
                        rs.getTimestamp("remind_at") != null
                                ? rs.getTimestamp("remind_at").toLocalDateTime()
                                : null)
                .isCompleted(rs.getBoolean("is_completed"))
                .isEnabled(rs.getBoolean("is_enabled"))
                .createdAt(
                        rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime()
                                : null)
                .build();
    }
}
