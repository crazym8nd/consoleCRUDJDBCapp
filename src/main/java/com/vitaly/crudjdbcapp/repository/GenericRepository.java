package com.vitaly.crudjdbcapp.repository;

import java.util.List;
import java.util.Optional;


public interface GenericRepository<T, ID> {
    Optional<T> getById(ID id);

    List<T> getAll();

    T save(T t);

    void update(T t);

    void deleteById(ID id);
}
