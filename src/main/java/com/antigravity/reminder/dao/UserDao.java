package com.antigravity.reminder.dao;

import com.antigravity.reminder.db.CustomConnectionPool;
import com.antigravity.reminder.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public Optional<User> get(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return getOne(sql, id);
    }

    public Optional<User> getByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return getOne(sql, username);
    }

    public Optional<User> getByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return getOne(sql, email);
    }

    private Optional<User> getOne(String sql, Object param) {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param instanceof Integer) {
                ps.setInt(1, (Integer) param);
            } else if (param instanceof String) {
                ps.setString(1, (String) param);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return users;
    }

    @Override
    public void save(User user) {
        String sql =
                "INSERT INTO users (username, email, password_hash, timezone, smtp_password) VALUES"
                        + " (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getTimezone());
            ps.setString(5, user.getSmtpPassword());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void update(User user) {
        String sql =
                "UPDATE users SET username = ?, email = ?, timezone = ?, smtp_password = ? WHERE id"
                        + " = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getTimezone());
            ps.setString(4, user.getSmtpPassword());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
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

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .passwordHash(rs.getString("password_hash"))
                .timezone(rs.getString("timezone"))
                .smtpPassword(rs.getString("smtp_password"))
                .createdAt(
                        rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime()
                                : null)
                .build();
    }
}
