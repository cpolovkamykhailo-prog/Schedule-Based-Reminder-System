package com.antigravity.reminder.dao;

import com.antigravity.reminder.db.CustomConnectionPool;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/** Advanced Generic DAO using reflection to reduce boilerplate. */
@Slf4j
public abstract class BaseReflectionDao<T> implements Dao<T> {
    protected final CustomConnectionPool pool = CustomConnectionPool.getInstance();
    private final Class<T> clazz;
    private final String tableName;

    protected BaseReflectionDao(Class<T> clazz, String tableName) {
        this.clazz = clazz;
        this.tableName = tableName;
    }

    @Override
    public Optional<T> get(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            log.error("Error fetching entity by id", e);
        } finally {
            pool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (Exception e) {
            log.error("Error fetching all entities", e);
        } finally {
            pool.releaseConnection(conn);
        }
        return entities;
    }

    // Helper to map ResultSet to Entity using reflection
    protected T mapResultSetToEntity(ResultSet rs) throws Exception {
        T entity = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String columnName = convertFieldToColumn(field.getName());
            try {
                Object value = rs.getObject(columnName);
                if (value instanceof Timestamp) {
                    field.set(entity, ((Timestamp) value).toLocalDateTime());
                } else {
                    field.set(entity, value);
                }
            } catch (SQLException e) {
                // Skip fields that don't match columns (like transient fields)
            }
        }
        return entity;
    }

    private String convertFieldToColumn(String fieldName) {
        // Simple camelCase to snake_case converter
        return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
