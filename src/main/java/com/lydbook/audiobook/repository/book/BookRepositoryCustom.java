package com.lydbook.audiobook.repository.book;

import javax.transaction.Transactional;

public interface BookRepositoryCustom {
    void deleteBookById(Long id);
}
