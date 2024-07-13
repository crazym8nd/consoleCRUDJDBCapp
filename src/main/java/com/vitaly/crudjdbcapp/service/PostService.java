package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;

import java.util.List;
import java.util.Optional;

/*
26-Dec-23
gh /crazym8nd
*/
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostService() {
        this.postRepository = new JDBCPostRepositoryImpl();
    }

    public Optional<Post> getById(Integer id) {
        return postRepository.getById(id);
    }

    public List<Post> getAll() {
        return postRepository.getAll();
    }

    public Post save(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        return postRepository.save(post);
    }

    public void update(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }
        postRepository.update(post);
    }

    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

}
