package com.lydbook.audiobook.repository.author;

import com.lydbook.audiobook.entity.Author;

public interface AuthorRepositoryCustom {
    Author findOrCreateAuthorByName(String name);
}
