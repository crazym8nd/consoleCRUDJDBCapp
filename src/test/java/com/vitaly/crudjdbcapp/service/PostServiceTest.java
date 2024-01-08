package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;

import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//  08-Jan-24
// gh crazym8nd

class PostServiceTest {

    private final JDBCPostRepositoryImpl postRepMock= mock(JDBCPostRepositoryImpl.class);
    private final PostService postService =new PostService(postRepMock);
    private final List<Label> postLabels = Arrays.asList(new Label (1, "label1", Status.ACTIVE), new Label (2, "label2", Status.ACTIVE), new Label (3, "label3", Status.ACTIVE));
    private final List<Post> mockPostList = Arrays.asList(new Post(1, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 1),
            new Post(2, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 1),
            new Post(3, "content", "created", "updated", PostStatus.ACTIVE, postLabels, 2));
    private Post mockPost = mockPostList.get(0);

//positive tests
    @Test
    void getByIdSuccess() {
        when(postRepMock.getById(1)).thenReturn(mockPostList.get(0));
        assertEquals(mockPost, postService.getById(1));
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
        when(postRepMock.getById(invlidId)).thenReturn(null);
        Post result = postService.getById(invlidId);
        assertNull(result);
    }

    @Test
    void getAllFail() {

        when(postRepMock.getAll()).thenReturn(null);
        List<Post> result = postService.getAll();
        assertNull(result);
    }

    @Test
    void saveFail() {
        mockPost = null;
        Post result = postService.save(mockPost);
        assertNull(result);
    }

    @Test
    void updateFail() {

        mockPost = null;
        Post result = postService.update(mockPost);
        assertNull(result);
    }

    @Test
    void deleteByIdFail() {

        int invlidId = 999;
        postService.deleteById(invlidId);
        verify(postRepMock, times(1)).deleteById(invlidId);
    }
}