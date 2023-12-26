package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;
import com.vitaly.crudjdbcapp.service.LabelService;

import java.util.List;

public class LabelController {
    private final LabelService labelService = new LabelService();

    public Label createLabel(String name) {
        Label createdLabel = new Label();
        createdLabel.setName(name);
        createdLabel.setStatus(Status.ACTIVE);
        return labelService.save(createdLabel);
    }

    public List<Label> getAll() {
        return labelService.getAll();
    }

    public Label getById(Integer id) {
        return labelService.getById(id);
    }

    public void update(Label label) {
        labelService.update(label);
    }

    public void deleteById(Integer id) {
        labelService.deleteById(id);
    }

}
