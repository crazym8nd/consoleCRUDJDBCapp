package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//  08-Jan-24
// gh crazym8nd

class PostServiceTest {

    private final JDBCPostRepositoryImpl postRepMock = mock(JDBCPostRepositoryImpl.class);
    private final PostService postService = new PostService(postRepMock);
    private final List<Label> postLabels = Arrays.asList(new Label(1, "label1", Status.ACTIVE), new Label(2, "label2", Status.ACTIVE), new Label(3, "label3", Status.ACTIVE));
    private final List<Post> mockPostList = Arrays.asList(new Post(1, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 1),
            new Post(2, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 1),
            new Post(3, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 2));
    private final Post mockPost = mockPostList.get(0);

    //positive tests
    @Test
    void getByIdSuccess() {
        when(postRepMock.getById(1)).thenReturn(Optional.of(mockPostList.get(0)));
        assertEquals(Optional.of(mockPost), postService.getById(1));

    }

    @Test
    void getAllSuccess() {
        when(postRepMock.getAll()).thenReturn(mockPostList);
        assertEquals(3, postService.getAll().size());
    }

    @Test
    void saveSuccess() {
        postService.save(mockPost);
        verify(postRepMock, times(1)).save(mockPost);
    }

    @Test
    void updateSuccess() {
        postService.update(mockPost);
        verify(postRepMock, times(1)).update(mockPost);
    }

    @Test
    void deleteByIdSuccess() {
        postService.deleteById(1);
        verify(postRepMock, times(1)).deleteById(1);
    }

    //negative tests
    @Test
    void getByIdFail() {

        int invlidId = 999;
        when(postRepMock.getById(invlidId)).thenReturn(Optional.empty());
        Optional<Post> result = postService.getById(invlidId);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllFail() {

        when(postRepMock.getAll()).thenReturn(null);
        List<Post> result = postService.getAll();
        assertNull(result);
    }

    @Test
    void saveFail() {
        assertThrows(IllegalArgumentException.class, () -> postService.save(null));
    }

    @Test
    void updateFail() {
        assertThrows(IllegalArgumentException.class, () -> postService.update(null));
    }

    @Test
    void deleteByIdFail() {

        int invlidId = 999;
        postService.deleteById(invlidId);
        verify(postRepMock, times(1)).deleteById(invlidId);
    }
}