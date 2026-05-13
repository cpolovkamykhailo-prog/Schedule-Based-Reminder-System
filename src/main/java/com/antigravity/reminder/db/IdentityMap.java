package com.antigravity.reminder.db;

import java.util.HashMap;
import java.util.Map;

/** Identity Map ensures that each object is loaded only once per session. */
public class IdentityMap<T> {
    private final Map<Integer, T> map = new HashMap<>();

    public void add(int id, T entity) {
        map.put(id, entity);
    }

    public T get(int id) {
        return map.get(id);
    }

    public void remove(int id) {
        map.remove(id);
    }

    public void clear() {
        map.clear();
    }
}
