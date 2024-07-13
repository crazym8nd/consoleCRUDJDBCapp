package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;

import java.util.List;
import java.util.Optional;


/*
26-Dec-23
gh /crazym8nd
*/
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public LabelService() {
        this.labelRepository = new JDBCLabelRepositoryImpl();
    }

    public Optional<Label> getById(Integer id) {
        return labelRepository.getById(id);
    }

    public List<Label> getAll() {
        return labelRepository.getAll();
    }

    public Label save(Label label) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
        return labelRepository.save(label);
    }

    public void update(Label label) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
        labelRepository.update(label);
    }

    public void deleteById(Integer id) {
        labelRepository.deleteById(id);
    }
}
