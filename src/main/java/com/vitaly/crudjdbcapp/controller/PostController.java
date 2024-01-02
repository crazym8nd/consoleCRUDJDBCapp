package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;
import com.vitaly.crudjdbcapp.service.PostService;

import java.util.ArrayList;
import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    public Post createPost(String   content) {
        Post createdpost = new Post();
        List<Label> postlabels = new ArrayList<>();
        postlabels.add(new Label(99, "Java", Status.ACTIVE));
        createdpost.setContent(content);
        createdpost.setPostStatus(PostStatus.ACTIVE);
     //   java.util.Date utilDate = new java.sql.Date(new java.util.Date().getTime());
        createdpost.setCreated("NOW");
        createdpost.setUpdated("NOW");
        createdpost.setPostLabels(postlabels);
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
