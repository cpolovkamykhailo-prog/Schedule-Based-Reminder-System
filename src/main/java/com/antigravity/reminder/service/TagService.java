package com.antigravity.reminder.service;

import com.antigravity.reminder.dao.TagDao;
import com.antigravity.reminder.model.Tag;
import java.util.List;

public class TagService {
    private final TagDao tagDao = new TagDao();
    private final com.antigravity.reminder.db.IdentityMap<Tag> identityMap =
            new com.antigravity.reminder.db.IdentityMap<>();

    public void createTag(String name, String color) {
        Tag tag = Tag.builder().name(name).color(color != null ? color : "#3498db").build();
        tagDao.save(tag);
        identityMap.clear(); // Clear cache after modification
    }

    public List<Tag> getAllTags() {
        List<Tag> tags = tagDao.getAll();
        tags.forEach(t -> identityMap.add(t.getId(), t));
        return tags;
    }

    public Tag getTag(int id) {
        Tag cached = identityMap.get(id);
        if (cached != null) return cached;

        return tagDao.get(id)
                .map(
                        t -> {
                            identityMap.add(t.getId(), t);
                            return t;
                        })
                .orElse(null);
    }

    public void deleteTag(int tagId) {
        tagDao.delete(tagId);
        identityMap.remove(tagId);
    }
}
