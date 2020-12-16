package com.lydbook.audiobook.repository;

import com.lydbook.audiobook.entity.Tag;

import javax.transaction.Transactional;
import java.util.List;

public interface TagRepositoryCustom {
    @Transactional
    Tag findOrCreateFirstByName(String name);
    @Transactional
    Tag findOrCreateFirstByName(String name, boolean isRemovable);
    void removeTagsWithNoBooks();
    List<Tag> findTagsWithNoBooks();
}
