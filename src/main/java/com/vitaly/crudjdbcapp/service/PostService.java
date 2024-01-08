package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;

import java.util.List;

/*
26-Dec-23
gh /crazym8nd
*/
public class PostService {

    private final PostRepository postRepository;
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    public PostService(){
        this.postRepository = new JDBCPostRepositoryImpl();
    }
    public Post getById(Integer id){
        return postRepository.getById(id);
    }
    public List<Post> getAll(){
        return postRepository.getAll();
    }
    public Post save (Post post){
        return postRepository.save(post);
    }
    public Post update(Post post){
        return postRepository.update(post);
    }
    public void deleteById(Integer id){
        postRepository.deleteById(id);
    }

}
