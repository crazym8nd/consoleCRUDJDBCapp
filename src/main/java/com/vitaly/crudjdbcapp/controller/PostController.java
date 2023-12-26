package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class PostController {
    private final PostRepository postRepository = new JDBCPostRepositoryImpl();

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
        return postRepository.save(createdpost);
    }
    public List<Post> getAll() {
        return postRepository.getAll();
    }

    public Post getById(Integer id) {
        return postRepository.getById(id);
    }
    public void update(Post post) {
        postRepository.update(post);
    }
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }
}
