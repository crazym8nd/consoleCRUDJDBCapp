package com.vitaly.crudjdbcapp.repository;

import java.util.List;
//repository - классы, реализующие доступ к БД

public interface GenericRepository<T, ID> {
    T getById(ID id);

    List<T> getAll();

    T save(T t);

    T update(T t);

    void deleteById(ID id);
}
