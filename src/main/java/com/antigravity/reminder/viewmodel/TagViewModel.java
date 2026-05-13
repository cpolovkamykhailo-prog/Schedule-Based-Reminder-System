package com.antigravity.reminder.viewmodel;

import com.antigravity.reminder.model.Tag;
import com.antigravity.reminder.service.TagService;
import java.util.List;

public class TagViewModel {
    private final TagService tagService = new TagService();
    private List<Tag> tags;

    public List<Tag> getTags() {
        if (tags == null) {
            refreshTags();
        }
        return tags;
    }

    public void refreshTags() {
        this.tags = tagService.getAllTags();
    }

    public void createTag(String name, String color) {
        tagService.createTag(name, color);
        refreshTags();
    }

    public void deleteTag(int tagId) {
        tagService.deleteTag(tagId);
        refreshTags();
    }
}
