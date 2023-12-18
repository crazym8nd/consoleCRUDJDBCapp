package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Writer;
import com.vitaly.crudjdbcapp.repository.WriterRepository;
import java.util.Collections;

import java.util.List;

public class JDBCWriterRepositoryImpl implements WriterRepository {

    @Override
    public Writer getById(Integer integer) {
        return null;
    }

    @Override
    public List<Writer> getAll() {
        return Collections.emptyList();
    }

    @Override
    public Writer save(Writer writer) {
        return null;
    }

    @Override
    public Writer update(Writer writer) {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }
}
