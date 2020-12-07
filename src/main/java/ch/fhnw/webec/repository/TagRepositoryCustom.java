package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Tag;

public interface TagRepositoryCustom {
    Tag findOrCreateFirstByName(String name);
    Tag findOrCreateFirstByName(String name, boolean isRemovable);
}
