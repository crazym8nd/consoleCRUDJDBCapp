package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.repository.impls.JDBCLabelRepositoryImpl;
import com.vitaly.crudjdbcapp.utils.LabelUtils;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LabelServiceTest {
    private final JDBCLabelRepositoryImpl labelRepMock = mock(JDBCLabelRepositoryImpl.class);
    private final LabelService labelService = new LabelService(labelRepMock);


    //happy path
    @Test
    @Name("Should return label by id")
    void givenCorrectId_whenGetById_thenReturnLabel() {
        when(labelRepMock.getById(1)).thenReturn(Optional.of(LabelUtils.getLabels().get(0)));
        assertEquals(Optional.of(LabelUtils.getLabel()), labelService.getById(1));
    }

    @Test
    @Name("Should return all labels")
    void givenListLabels_whenGetAll_thenReturnLabels() {
        when(labelRepMock.getAll()).thenReturn(LabelUtils.getLabels());
        assertEquals(3, labelService.getAll().size());
    }

    @Test
    @Name("Should save label")
    void givenLabelForSave_whenSave_thenReturnLabel() {
        labelService.save(LabelUtils.getLabel());
        verify(labelRepMock, times(1)).save(LabelUtils.getLabel());
    }

    @Test
    @Name("Should update label")
    void givenLabelForUpdate_whenUpdate_thenSuccess() {
        labelService.update(LabelUtils.getLabel());
        verify(labelRepMock, times(1)).update(LabelUtils.getLabel());
    }

    @Test
    @Name("Should delete label")
    void givenLabelForDelete_whenDelete_thenSuccess() {
        labelService.deleteById(1);
        verify(labelRepMock, times(1)).deleteById(1);
    }

    //negative tests
    @Test
    @Name("Should return empty when id is invalid")
    void givenInvalidId_whenGetById_thenReturnEmpty() {
        int invlidId = 999;
        when(labelRepMock.getById(invlidId)).thenReturn(Optional.empty());
        Optional<Label> result = labelService.getById(invlidId);
        assertTrue(result.isEmpty());
    }

    @Test
    @Name("Should return empty list when no labels in db")
    void givenNoLabels_whenGetAll_thenReturnEmptyList() {
        when(labelRepMock.getAll()).thenReturn(Collections.emptyList());
        List<Label> result = labelService.getAll();
        assertTrue(result.isEmpty());
    }

    @Test
    @Name("Should throw exception when save null label")
    void givenNullLabel_whenSave_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> labelService.save(null));
    }

    @Test
    @Name("Should throw exception when update null label")
    void givenNullLabel_whenUpdate_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> labelService.update(null));
    }

    @Test
    @Name("Should call repository")
    void givenInvalidId_whenDelete_thenCallRep() {
        int invalidId = 999;
        labelService.deleteById(invalidId);
        verify(labelRepMock, times(1)).deleteById(invalidId);
    }


}