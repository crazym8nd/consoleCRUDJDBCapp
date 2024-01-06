package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;
import lombok.SneakyThrows;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.*;

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
    private static final String INSERT_POST_LABELS = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";
    private static final String INSERT_WRITER_POSTS = "INSERT INTO writer_posts (writer_id, post_id) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + POST_TABLE  + " SET  content = ?, created = ?, updated = ?, post_status = ?, writer_id = ? WHERE id = ?";
    private static final String UPDATE_POST_LABELS_QUERY = "UPDATE post_labels SET label_id = ? WHERE post_id = ?";
    private static final String UPDATE_WRITER_POSTS_QUERY = "UPDATE writer_posts SET writer_id = ? WHERE post_id = ?";

    private static final String DELETE_QUERY = "UPDATE " + POST_TABLE  + " SET post_status = ? WHERE id = ?";

    @SneakyThrows
    @Override
    public Post getById(Integer integer) {
        Post post = null;

        try(PreparedStatement statement = JDBCUtil.getPreparedStatement(GET_BY_ID_QUERY)){
            statement.setInt(1, integer);
            ResultSet rs = statement.executeQuery();
            Map<Integer, Post> postMap = new HashMap<>();

            while (rs.next()) {
                int id = rs.getInt("p.id");
                if (!postMap.containsKey(id)) {
                    List<Label> postLabels = new ArrayList<>();

                    String content = rs.getString("content");
                    String created = rs.getString("created");
                    String updated = rs.getString("updated");
                    String postStatus = rs.getString("post_status");

                    post = Post.builder()
                            .id(id)
                            .content(content)
                            .created(created)
                            .updated(updated)
                            .postStatus(PostStatus.valueOf(postStatus))
                            .postLabels(postLabels)
                            .build();

                    postMap.put(id, post);
                }
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


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return post;
    }





    @SneakyThrows
    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try(PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(READ_QUERY)){
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
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables.getMessage());
        }
        return posts;
    }

    @SneakyThrows
    @Override
    public Post save(Post post) {
        try {
            Connection connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {


                preparedStatement.setString(1, post.getContent());
                preparedStatement.setString(2, post.getCreated());
                preparedStatement.setString(3, post.getUpdated());
                preparedStatement.setString(4, post.getPostStatus().toString());
                preparedStatement.setInt(5, post.getWriterId());



                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    post.setId(id);
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_POST_LABELS)){
            preparedStatement.setInt(1, post.getId());
            preparedStatement.setInt(2, post.getPostLabels().get(0).getId());
            preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WRITER_POSTS)){
                preparedStatement.setInt(1, post.getWriterId());
                preparedStatement.setInt(2, post.getId());
                preparedStatement.executeUpdate();
            }

                try {
                    connection.commit();
                } catch (SQLException throwables) {
                    connection.rollback();
                    throwables.printStackTrace();
                }
            }

            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return post;

        }


        @Override
    public Post update(Post post) {
        try {
            Connection connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(UPDATE_QUERY)) {

                preparedStatement.setString(1, post.getContent());
                preparedStatement.setString(2, post.getCreated());
                preparedStatement.setString(3, post.getUpdated());
                preparedStatement.setString(4, post.getPostStatus().toString());
                preparedStatement.setInt(5, post.getWriterId());
                preparedStatement.setInt(6, post.getId());

                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(UPDATE_POST_LABELS_QUERY)) {

                preparedStatement.setInt(1, post.getPostLabels().get(0).getId());
                preparedStatement.setInt(2, post.getId());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(UPDATE_WRITER_POSTS_QUERY)) {

                preparedStatement.setInt(1, post.getWriterId());
                preparedStatement.setInt(2, post.getId());
                preparedStatement.executeUpdate();
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
    public void deleteById(Integer integer) {
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(DELETE_QUERY)) {
            Connection connection = JDBCUtil.getConnection();
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

    private static Post mapToPost(ResultSet rs) throws SQLException {
        int id = rs.getInt("p.id");
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

        return post;
    }
}
