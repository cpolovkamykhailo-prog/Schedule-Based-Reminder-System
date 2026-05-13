package com.antigravity.reminder.dao;

import com.antigravity.reminder.db.CustomConnectionPool;
import com.antigravity.reminder.model.Schedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScheduleDao implements Dao<Schedule> {
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public Optional<Schedule> get(int id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToSchedule(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Schedule> getAll() {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return schedules;
    }

    @Override
    public void save(Schedule schedule) {
        String sql =
                "INSERT INTO schedules (reminder_id, user_id, name, frequency, \"interval\","
                        + " start_time, next_run) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (schedule.getReminderId() != null) ps.setInt(1, schedule.getReminderId());
            else ps.setNull(1, Types.INTEGER);
            if (schedule.getUserId() != null) ps.setInt(2, schedule.getUserId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, schedule.getName());
            ps.setString(4, schedule.getFrequency());
            ps.setInt(5, schedule.getInterval() != null ? schedule.getInterval() : 1);
            ps.setTimestamp(
                    6,
                    schedule.getStartTime() != null
                            ? Timestamp.valueOf(schedule.getStartTime())
                            : null);
            ps.setTimestamp(
                    7,
                    schedule.getNextRun() != null
                            ? Timestamp.valueOf(schedule.getNextRun())
                            : null);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void update(Schedule schedule) {
        String sql =
                "UPDATE schedules SET reminder_id = ?, user_id = ?, name = ?, frequency = ?,"
                    + " \"interval\" = ?, start_time = ?, next_run = ?, last_run = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (schedule.getReminderId() != null) ps.setInt(1, schedule.getReminderId());
            else ps.setNull(1, Types.INTEGER);
            if (schedule.getUserId() != null) ps.setInt(2, schedule.getUserId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, schedule.getName());
            ps.setString(4, schedule.getFrequency());
            ps.setInt(5, schedule.getInterval() != null ? schedule.getInterval() : 1);
            ps.setTimestamp(
                    6,
                    schedule.getStartTime() != null
                            ? Timestamp.valueOf(schedule.getStartTime())
                            : null);
            ps.setTimestamp(
                    7,
                    schedule.getNextRun() != null
                            ? Timestamp.valueOf(schedule.getNextRun())
                            : null);
            ps.setTimestamp(
                    8,
                    schedule.getLastRun() != null
                            ? Timestamp.valueOf(schedule.getLastRun())
                            : null);
            ps.setInt(9, schedule.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
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

    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        return Schedule.builder()
                .id(rs.getInt("id"))
                .reminderId(rs.getObject("reminder_id", Integer.class))
                .userId(rs.getObject("user_id", Integer.class))
                .name(rs.getString("name"))
                .frequency(rs.getString("frequency"))
                .interval(rs.getInt("interval"))
                .startTime(
                        rs.getTimestamp("start_time") != null
                                ? rs.getTimestamp("start_time").toLocalDateTime()
                                : null)
                .nextRun(
                        rs.getTimestamp("next_run") != null
                                ? rs.getTimestamp("next_run").toLocalDateTime()
                                : null)
                .lastRun(
                        rs.getTimestamp("last_run") != null
                                ? rs.getTimestamp("last_run").toLocalDateTime()
                                : null)
                .build();
    }
}
