package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.service.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCPostRepositoryImpl implements PostRepository {

    private static final String POST_TABLE = "posts";

    private static final String LABELS_FROM_POST_PARSE = "SELECT crudapp.labels.name\n" +
            "FROM crudapp.post_label\n" +
            "JOIN crudapp.labels ON post_label.label_id = crudapp.labels.id\n" +
            "WHERE post_label.post_id = 1";
    private static final String READ_QUERY = "SELECT * FROM " + POST_TABLE  + " WHERE poststatus = 'ACTIVE'";
    private static final String INSERT_QUERY = "INSERT INTO " + POST_TABLE  + "(name, poststatus) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + POST_TABLE  + " SET name = ?, poststatus = ? WHERE id = ?";
    private static final String DELETE_QUERY = "UPDATE " + POST_TABLE  + " SET poststatus = ? WHERE id = ?";

    private static List<Post> getPostsData(String query) {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet rs = preparedStatement.executeQuery();


                while (rs.next()) {
                    int id = rs.getInt("id");
                    String content = rs.getString("content");
                    String created = rs.getString("created");
                    String updated = rs.getString("updated");
                    String postStatus = rs.getString("postStatus");
                    List<Label> postlabels = null;
                    posts.add(new Post(id, content, created, updated, postlabels, (PostStatus.valueOf(postStatus))));
                }

                try {
                    connection.commit();

                } catch (SQLException throwables)
                {
                    connection.rollback();
                    throwables.printStackTrace();
                }
            }} catch (SQLException throwables) {
            try {
                throw throwables;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
            return posts;
    }


    @Override
    public Post getById(Integer integer) {
        List<Post> posts = getPostsData(READ_QUERY);
        return posts.stream().
                filter(p -> p.getId().equals(integer)).
                findFirst().orElse(null);
        }



    @Override
    public List<Post> getAll() {
        return getPostsData(READ_QUERY);
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
