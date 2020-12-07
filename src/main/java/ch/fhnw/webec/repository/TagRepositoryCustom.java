package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Tag;

public interface TagRepositoryCustom {
    Tag findFirstByName(String name);
    Tag findFirstByName(String name, boolean isRemovable);
}
