package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LabelServiceTest {


    private JDBCLabelRepositoryImpl labelRepMock= mock(JDBCLabelRepositoryImpl.class);
    private LabelService labelService =new LabelService(labelRepMock);
    private List<Label> mockList = Arrays.asList(new Label(1, "label1", Status.ACTIVE),
            new Label(2, "label2", Status.ACTIVE),
            new Label(3, "label3", Status.ACTIVE));
    private Label mockLabel = mockList.get(0);


    //positive tests
    @Test
    void getByIdTestSuccess() {
        when(labelRepMock.getById(1)).thenReturn(mockList.get(0));
        assertEquals(mockLabel, labelService.getById(1));
    }

    @Test
    void getAllTestSuccess() {
        when(labelRepMock.getAll()).thenReturn(mockList);
        assertEquals(3, labelService.getAll().size());
    }

    @Test
    void saveTestSuccess() {
       labelService.save(mockLabel);
        verify(labelRepMock, times(1)).save(mockLabel);
    }

    @Test
    void updateTestSuccess() {
        labelService.update(mockLabel);
        verify(labelRepMock, times(1)).update(mockLabel);
    }

    @Test
    void deleteByIdTestSuccess() {
        labelService.deleteById(1);
        verify(labelRepMock, times(1)).deleteById(1);
    }

    //negative tests
    @Test
    void getByIdTestFail() {
        int invlidId = 999;
        when(labelRepMock.getById(invlidId)).thenReturn(null);
        Label result = labelService.getById(invlidId);
        assertNull(result);
    }

    @Test
    void getAllTestFail() {
        when(labelRepMock.getAll()).thenReturn(null);
        List<Label> result = labelService.getAll();
        assertNull(result);
    }

    @Test
    void saveTestFail() {
        mockLabel = null;
        Label result = labelService.save(mockLabel);
        assertNull(result);
    }

    @Test
    void updateTestFail() {
        mockLabel = null;
        Label result = labelService.update(mockLabel);
        assertNull(result);
    }

    @Test
    void deleteByIdTestFail() {
        int invlidId = 999;
        labelService.deleteById(invlidId);
        verify(labelRepMock, times(1)).deleteById(invlidId);
    }

}