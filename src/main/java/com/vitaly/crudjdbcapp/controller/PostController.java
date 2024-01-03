package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;
import com.vitaly.crudjdbcapp.service.PostService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    public Post createPost(String   content, List<Label> postLabels) {
        Post createdpost = new Post();
        createdpost.setContent(content);
        createdpost.setPostStatus(PostStatus.ACTIVE);
        createdpost.setCreated(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
        createdpost.setUpdated("NEW");
        createdpost.setPostLabels(postLabels);
        return postService.save(createdpost);
    }
    public List<Post> getAll() {
        return postService.getAll();
    }

    public Post getById(Integer id) {
        return postService.getById(id);
    }
    public void update(Post post) {
        postService.update(post);
    }
    public void deleteById(Integer id) {
        postService.deleteById(id);
    }
}
