package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import java.util.Collections;

import java.util.List;

public class JDBCPostRepositoryImpl implements PostRepository {
    @Override
    public Post getById(Integer integer) {
        return null;
    }

    @Override
    public List<Post> getAll() {
        return Collections.emptyList();
    }

    @Override
    public Post save(Post post) {
        return null;
    }

    @Override
    public Post update(Post post) {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }
}
