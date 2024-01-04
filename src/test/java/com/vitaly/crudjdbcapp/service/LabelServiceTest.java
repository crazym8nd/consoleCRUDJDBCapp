package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
04-Jan-24
gh /crazym8nd
*/class LabelServiceTest {

    private JDBCLabelRepositoryImpl labelRepMock;
    private LabelService labelService;
    private List<Label> mockList;

    @BeforeEach
    public void setup(){
        labelRepMock = mock(JDBCLabelRepositoryImpl.class);
        labelService = new LabelService(labelRepMock);
        mockList = Arrays.asList(new Label(1, "label1", Status.ACTIVE), new Label(2, "label2", Status.ACTIVE), new Label(3, "label3", Status.ACTIVE));

        when(labelRepMock.getAll()).thenReturn(mockList);
    }

    @Test
    void ShouldReturnLabelById_getById() {
        when(labelRepMock.getById(1)).thenReturn(mockList.get(1));
        assertEquals(mockList.get(1), labelService.getById(1));
    }

    @Test
    void getAll() {
        assertEquals(3, labelService.getAll().size());
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