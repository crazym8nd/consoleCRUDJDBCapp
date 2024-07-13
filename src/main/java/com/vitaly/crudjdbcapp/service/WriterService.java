package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Writer;
import com.vitaly.crudjdbcapp.repository.WriterRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCWriterRepositoryImpl;

import java.util.List;
import java.util.Optional;

/*
26-Dec-23
gh /crazym8nd
*/
public class WriterService {
    private final WriterRepository writerRepository;

    public WriterService(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public WriterService() {
        this.writerRepository = new JDBCWriterRepositoryImpl();
    }

    public Optional<Writer> getById(Integer id) {
        return writerRepository.getById(id);
    }

    public List<Writer> getAll() {
        return writerRepository.getAll();
    }

    public Writer save(Writer writer) {
        if (writer == null) {
            throw new IllegalArgumentException("Writer cannot be null");
        }
        return writerRepository.save(writer);
    }

    public void update(Writer writer) {
        if (writer == null) {
            throw new IllegalArgumentException("Writer cannot be null");
        }
        writerRepository.update(writer);
    }

    public void deleteById(Integer id) {
        writerRepository.deleteById(id);
    }
}
