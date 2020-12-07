package ch.fhnw.webec.repository;

import ch.fhnw.webec.entity.Author;

public interface AuthorRepositoryCustom {
    Author findOrCreateAuthorByName(String name);
}
