package com.antigravity.reminder.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic DAO interface.
 *
 * @param <T> the entity type
 */
public interface Dao<T> {
    Optional<T> get(int id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(int id);
}
