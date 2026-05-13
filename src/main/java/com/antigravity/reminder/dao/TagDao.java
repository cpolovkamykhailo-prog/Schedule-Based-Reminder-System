package com.antigravity.reminder.dao;

import com.antigravity.reminder.model.Tag;
import java.sql.*;

public class TagDao extends com.antigravity.reminder.dao.BaseReflectionDao<Tag> {

    public TagDao() {
        super(Tag.class, "tags");
    }

    @Override
    public void save(Tag tag) {
        String sql = "INSERT INTO tags (name, color) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tag.getName());
            ps.setString(2, tag.getColor());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void update(Tag tag) {
        String sql = "UPDATE tags SET name = ?, color = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tag.getName());
            ps.setString(2, tag.getColor());
            ps.setInt(3, tag.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM tags WHERE id = ?";
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
}
