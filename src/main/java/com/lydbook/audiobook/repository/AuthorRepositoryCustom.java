package com.lydbook.audiobook.repository;

import com.lydbook.audiobook.entity.Author;

public interface AuthorRepositoryCustom {
    Author findOrCreateAuthorByName(String name);
}
