package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;

// vie - controller - service - repository - bd
/*
26-Dec-23
gh /crazym8nd
*/
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }
    public LabelService(){
        this.labelRepository = new JDBCLabelRepositoryImpl();
    }
    public Label getById(Integer id){
        return labelRepository.getById(id);
    }
}
