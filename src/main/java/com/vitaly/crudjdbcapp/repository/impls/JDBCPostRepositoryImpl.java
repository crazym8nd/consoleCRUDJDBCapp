package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.exceptions.NoPostsInDbException;
import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.PostStatus;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.PostRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;

import java.sql.*;
import java.util.*;


public class JDBCPostRepositoryImpl implements PostRepository {
    private final Connection connection;
    private static final String POST_TABLE = "posts";

    private static final String READ_QUERY = "SELECT * FROM " + POST_TABLE + " p " +
            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
            "LEFT JOIN labels l on pl.label_id = l.id " +
            "WHERE p.post_status = 'ACTIVE' ";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM " + POST_TABLE + " p " +
            "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
            "LEFT JOIN labels l on pl.label_id = l.id " +
            "WHERE p.post_status = 'ACTIVE' AND p.id = ? ";

    private static final String INSERT_QUERY = "INSERT INTO " + POST_TABLE + "( content, created, updated, post_status, writer_id) VALUES ( ?, ?, ?,?, ?)";
    private static final String INSERT_POST_LABELS = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";
    private static final String INSERT_WRITER_POSTS = "INSERT INTO writer_posts (writer_id, post_id) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + POST_TABLE + " SET  content = ?, created = ?, updated = ?, post_status = ?, writer_id = ? WHERE id = ?";
    private static final String UPDATE_POST_LABELS_QUERY = "UPDATE post_labels SET label_id = ? WHERE post_id = ?";
    private static final String UPDATE_WRITER_POSTS_QUERY = "UPDATE writer_posts SET writer_id = ? WHERE post_id = ?";

    private static final String DELETE_QUERY = "UPDATE " + POST_TABLE + " SET post_status = ? WHERE id = ?";

    public JDBCPostRepositoryImpl() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Post> getById(Integer integer) {
        try {
            try (PreparedStatement statement = JDBCUtil.getPreparedStatement(GET_BY_ID_QUERY)) {
                statement.setInt(1, integer);
                ResultSet rs = statement.executeQuery();
                Map<Integer, Post> postMap = new HashMap<>();

                while (rs.next()) {
                    int id = rs.getInt("p.id");
                    Post post;
                    if (!postMap.containsKey(id)) {
                        post = mapToPost(rs);
                        postMap.put(id, post);
                    } else {
                        post = postMap.get(id);
                    }
                    String status = rs.getString("status");
                    if (status != null && status.equals("ACTIVE")) {
                        post.getPostLabels().add(mapToLabel(rs));
                    }
                }
                return Optional.ofNullable(postMap.get(integer));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(READ_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                throw new NoPostsInDbException("No posts found in the database");
            }

            Map<Integer, Post> postMap = new HashMap<>();
            do {
                int id = rs.getInt("p.id");
                if (!postMap.containsKey(id)) {
                    Post post = mapToPost(rs);
                    postMap.put(id, post);
                    posts.add(post);
                }

                String status = rs.getString("status");
                if (status != null && status.equals("ACTIVE")) {
                    List<Label> postLabels = postMap.get(id).getPostLabels();
                    postLabels.add(mapToLabel(rs));
                }
            } while (rs.next());
            return posts;
        } catch (NoPostsInDbException e) {
            System.out.println("No posts found in the database");
            return Collections.emptyList();
        } catch (SQLException e) {
            throw new RuntimeException(e.getSQLState());
        }
    }


    @Override
    public Post save(Post post) {
        try {
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_POST_LABELS)) {
                preparedStatement.setInt(1, post.getId());
                preparedStatement.setInt(2, post.getPostLabels().get(0).getId());
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WRITER_POSTS)) {
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
    public void update(Post post) {
        try {
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
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteById(Integer integer) {
        Optional<Post> postOpt = getById(integer);
        if (postOpt.isPresent() && postOpt.get().getPostStatus() != null) {
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(DELETE_QUERY)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, Status.DELETED.toString());
            preparedStatement.setInt(2, integer);
            preparedStatement.executeUpdate();


                connection.commit();
                connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
        } else {
            throw new NoSuchElementException("Post with such id is not existing");
        }
    }

    private Post mapToPost(ResultSet rs) throws SQLException {
        List<Label> postLabels = new ArrayList<>();
        int id = rs.getInt("p.id");
        String content = rs.getString("content");
        String created = rs.getString("created");
        String updated = rs.getString("updated");
        String postStatus = rs.getString("post_status");

        return Post.builder()
                .id(id)
                .content(content)
                .created(created)
                .updated(updated)
                .postStatus(PostStatus.valueOf(postStatus))
                .postLabels(postLabels)
                .build();
    }

    private Label mapToLabel(ResultSet rs) throws SQLException {
        int labelId = rs.getInt("label_id");
        String labelName = rs.getString("name");
        String status = rs.getString("status");

        return Label.builder()
                .id(labelId)
                .name(labelName)
                .status(Status.valueOf(status))
                .build();
    }
}
