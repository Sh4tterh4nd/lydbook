package com.lydbook.audiobook.repository;

import javax.transaction.Transactional;

public interface BookRepositoryCustom {
    void deleteBookById(Long id);
}
