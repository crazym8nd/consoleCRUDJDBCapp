package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCPostRepositoryImpl implements PostRepository {

    private static final String POST_TABLE = "posts";

    private static final String READ_QUERY = "SELECT * FROM " + POST_TABLE  + " p " +
            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
            "LEFT JOIN labels l on pl.label_id = l.id " +
            "WHERE p.post_status = 'ACTIVE' ";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM " + POST_TABLE  + " p " +
            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
            "LEFT JOIN labels l on pl.label_id = l.id " +
            "WHERE p.post_status = 'ACTIVE' AND p.id = ? ";

    private static final String INSERT_QUERY = "INSERT INTO " + POST_TABLE  + "( content, created, updated, post_status, writer_id) VALUES ( ?, ?, ?,?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + POST_TABLE  + " SET  content = ?, created = ?, updated = ?, post_status = ? WHERE id = ?";
    private static final String DELETE_QUERY = "UPDATE " + POST_TABLE  + " SET post_status = ? WHERE id = ?";

    private static List<Post> getPostsData(String query) {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                connection.setAutoCommit(false);
                ResultSet rs = preparedStatement.executeQuery();
                Map<Integer, Post> postMap = new HashMap<>();

                while (rs.next()) {
                    int id = rs.getInt("p.id");
//TODO mapping v otdelnij method
                    if (!postMap.containsKey(id)) {
                        List<Label> postLabels = new ArrayList<>();

                        String content = rs.getString("content");
                        String created = rs.getString("created");
                        String updated = rs.getString("updated");
                        String postStatus = rs.getString("post_status");

                        Post post = Post.builder()
                                .id(id)
                                .content(content)
                                .created(created)
                                .updated(updated)
                                .postStatus(PostStatus.valueOf(postStatus))
                                .postLabels(postLabels)
                                .build();

                        postMap.put(id, post);
                        posts.add(post);
                    }
//TODO null checks
                    String status = rs.getString("status");
                    if (status != null && status.equals("ACTIVE")) {
                        int labelId = rs.getInt("label_id");
                        String labelName = rs.getString("name");
                        List<Label> postLabels = postMap.get(id).getPostLabels();
                        postLabels.add(Label.builder()
                                .id(labelId)
                                .name(labelName)
                                .status(Status.valueOf(status))
                                .build());
                    }
                }

                try {
                    connection.commit();
                } catch (SQLException throwables) {
                    connection.rollback();
                    throwables.printStackTrace();
                }
            } catch (SQLException throwables) {
                try {
                    throw throwables;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        return posts;
    }


    @SneakyThrows
    @Override
    public Post getById(Integer integer) {
        return null;
        } //TODO



    @SneakyThrows
    @Override
    public List<Post> getAll() {
        return getPostsData(READ_QUERY);
    }

    @Override
    public Post save(Post post) {
        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, post.getContent());
            preparedStatement.setString(2, post.getCreated());
            preparedStatement.setString(3, post.getUpdated());
            preparedStatement.setString(4, post.getPostStatus().toString());
            preparedStatement.setInt(5, 1);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                int id = generatedKeys.getInt(1);
                post.setId(id);
            }

            try {
                connection.commit();
            } catch (SQLException throwables) {
                connection.rollback();
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return post;
    }

    @Override
    public Post update(Post post) {
        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            connection.setAutoCommit(false);


            preparedStatement.setString(1, post.getContent());
            preparedStatement.setString(2, post.getCreated());
            preparedStatement.setString(3, post.getUpdated());
            preparedStatement.setString(4, post.getPostStatus().toString());
            preparedStatement.setInt(5, post.getId());

            preparedStatement.executeUpdate();

            try {
                connection.commit();
            } catch (SQLException throwables) {
                connection.rollback();
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return post;
    }

    @Override
    public void deleteById(Integer integer) {
        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, Status.DELETED.toString());
            preparedStatement.setInt(2, integer);
            preparedStatement.executeUpdate();

            try {
                connection.commit();
            } catch (SQLException throwables) {
                connection.rollback();
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
