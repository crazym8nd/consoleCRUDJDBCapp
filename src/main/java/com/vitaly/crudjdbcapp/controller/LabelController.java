package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;

import java.util.List;

public class LabelController {
    private final LabelRepository labelRep = new JDBCLabelRepositoryImpl();

    public Label createLabel(String name) {
        Label createdLabel = new Label();
        createdLabel.setName(name);
        createdLabel.setStatus(PostStatus.ACTIVE);
        return labelRep.save(createdLabel);
    }

    public List<Label> getAll() {
        return labelRep.getAll();
    }

    public Label getById(Integer id) {
        return labelRep.getById(id);
    }

    public void update(Label label) {
        labelRep.update(label);
    }

    public void deleteById(Integer id) {
        labelRep.deleteById(id);
    }

}
