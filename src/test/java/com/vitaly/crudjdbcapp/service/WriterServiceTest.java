package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.*;
import com.vitaly.crudjdbcapp.repository.impls.JDBCWriterRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//  08-Jan-24
// gh crazym8nd

class WriterServiceTest {

    private final JDBCWriterRepositoryImpl writerRepMock = mock(JDBCWriterRepositoryImpl.class);
    private final WriterService writerService = new WriterService(writerRepMock);
    private final List<Label> postLabels = Arrays.asList(new Label(1, "label1", Status.ACTIVE), new Label(2, "label2", Status.ACTIVE), new Label(3, "label3", Status.ACTIVE));
    private final List<Post> mockPostList = Arrays.asList(new Post(1, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 1),
            new Post(2, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 1),
            new Post(3, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 2));
    private final List<Writer> mockWriterList = Arrays.asList(new Writer(1, "writer1", "created", mockPostList, Status.ACTIVE),
            new Writer(2, "writer2", "created", mockPostList, Status.ACTIVE),
            new Writer(3, "writer3", "created", mockPostList, Status.ACTIVE));
    private final Writer mockWriter = mockWriterList.get(0);

    //positive tests
    @Test
    void getByIdSuccess() {
        when(writerRepMock.getById(1)).thenReturn(Optional.of(mockWriterList.get(0)));
        assertEquals(Optional.of(mockWriter), writerService.getById(1));
    }

    @Test
    void getAllSuccess() {
        when(writerRepMock.getAll()).thenReturn(mockWriterList);
        assertEquals(3, writerService.getAll().size());
    }

    @Test
    void saveSuccess() {
        writerService.save(mockWriter);
        verify(writerRepMock, times(1)).save(mockWriter);
    }

    @Test
    void updateSuccess() {
        writerService.update(mockWriter);
        verify(writerRepMock, times(1)).update(mockWriter);
    }

    @Test
    void deleteByIdSuccess() {
        writerService.deleteById(1);
        verify(writerRepMock, times(1)).deleteById(1);
    }

    //negative tests
    @Test
    void getByIdFail() {
        int invalidId = 999;
        when(writerRepMock.getById(invalidId)).thenReturn(Optional.empty());
        Optional<Writer> result = writerService.getById(invalidId);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllFail() {
        when(writerRepMock.getAll()).thenReturn(null);
        List<Writer> result = writerService.getAll();
        assertNull(result);
    }

    @Test
    void saveFail() {
        assertThrows(IllegalArgumentException.class, () -> writerService.save(null));
    }

    @Test
    void updateFail() {
        assertThrows(IllegalArgumentException.class, () -> writerService.update(null));
    }

    @Test
    void deleteByIdFail() {
        int invlidId = 999;
        writerService.deleteById(invlidId);
        verify(writerRepMock, times(1)).deleteById(invlidId);
    }
}