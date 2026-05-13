package com.antigravity.reminder.dao;

import com.antigravity.reminder.db.CustomConnectionPool;
import com.antigravity.reminder.model.NotificationLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificationLogDao implements Dao<NotificationLog> {
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public Optional<NotificationLog> get(int id) {
        String sql = "SELECT * FROM notification_logs WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLog(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<NotificationLog> getAll() {
        List<NotificationLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM notification_logs";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return logs;
    }

    @Override
    public void save(NotificationLog log) {
        String sql =
                "INSERT INTO notification_logs (reminder_id, status, channel_type, error_message)"
                        + " VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, log.getReminderId());
            ps.setString(2, log.getStatus());
            ps.setString(3, log.getChannelType());
            ps.setString(4, log.getErrorMessage());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void update(NotificationLog log) {
        String sql = "UPDATE notification_logs SET status = ?, error_message = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, log.getStatus());
            ps.setString(2, log.getErrorMessage());
            ps.setInt(3, log.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM notification_logs WHERE id = ?";
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

    private NotificationLog mapResultSetToLog(ResultSet rs) throws SQLException {
        return NotificationLog.builder()
                .id(rs.getInt("id"))
                .reminderId(rs.getInt("reminder_id"))
                .sentAt(
                        rs.getTimestamp("sent_at") != null
                                ? rs.getTimestamp("sent_at").toLocalDateTime()
                                : null)
                .status(rs.getString("status"))
                .channelType(rs.getString("channel_type"))
                .errorMessage(rs.getString("error_message"))
                .build();
    }
}
