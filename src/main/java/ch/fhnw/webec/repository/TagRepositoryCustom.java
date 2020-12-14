package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Tag;

import java.util.List;

public interface TagRepositoryCustom {
    Tag findOrCreateFirstByName(String name);
    Tag findOrCreateFirstByName(String name, boolean isRemovable);
    void removeTagsWithNoBooks();
    List<Tag> findTagsWithNoBooks();
}
