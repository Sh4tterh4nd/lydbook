package ch.fhnw.webec.repository;

import javax.transaction.Transactional;

public interface BookRepositoryCustom {
    void deleteBookById(Long id);
}
