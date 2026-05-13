package com.antigravity.reminder.db;

import com.antigravity.reminder.dao.Dao;
import java.util.ArrayList;
import java.util.List;

/** Unit of Work tracks changes to entities and commits them as a single block. */
public class UnitOfWork<T> {
    private final List<T> newEntities = new ArrayList<>();
    private final List<T> updatedEntities = new ArrayList<>();
    private final List<Integer> deletedIds = new ArrayList<>();
    private final Dao<T> dao;

    public UnitOfWork(Dao<T> dao) {
        this.dao = dao;
    }

    public void registerNew(T entity) {
        newEntities.add(entity);
    }

    public void registerDirty(T entity) {
        updatedEntities.add(entity);
    }

    public void registerDeleted(int id) {
        deletedIds.add(id);
    }

    public void commit() {
        newEntities.forEach(dao::save);
        updatedEntities.forEach(dao::update);
        deletedIds.forEach(dao::delete);

        newEntities.clear();
        updatedEntities.clear();
        deletedIds.clear();
    }
}
