package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.service.LabelService;

import java.util.List;
import java.util.Optional;

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

    public Optional<Label> getById(Integer id) {
        return labelService.getById(id);
    }

    public void update(Label label) {
        labelService.update(label);
    }

    public void deleteById(Integer id) {
        labelService.deleteById(id);
        boolean exists = labelService.getById(id).isPresent();
        if (exists) {
            System.out.println("Failed to delete label with ID: " + id);
        } else {
            System.out.println("Label with ID " + id + " has been successfully deleted.");
        }
    }

}
