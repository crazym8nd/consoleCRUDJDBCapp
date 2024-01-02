package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/*
02-Jan-24
gh /crazym8nd
*/

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {
    @InjectMocks
    private LabelService labelService;

    @Mock
    private LabelRepository labelRepository;

    @Test
    void getById_shouldReturnlabel() {

        //get
       int labelId = 99;
       Label label = new Label(labelId, "testlabel", Status.ACTIVE);

       Mockito.lenient().when(labelRepository.getById(labelId)).thenReturn(label);


    }
    @Test
    void getById_noSUchId(){
        //get
        int labelId = 99;
        int lookForId = 88;
        Label label = new Label(labelId, "testlabel", Status.ACTIVE);

        Mockito.lenient().when(labelRepository.getById(lookForId)).thenReturn(null);
    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}