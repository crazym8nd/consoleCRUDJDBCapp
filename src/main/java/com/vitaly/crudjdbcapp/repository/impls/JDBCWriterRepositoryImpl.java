package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.*;
import com.vitaly.crudjdbcapp.repository.WriterRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.*;

public class JDBCWriterRepositoryImpl implements WriterRepository {

    private static final String WRITER_TABLE = "writers";

    private static final String READ_QUERY = "SELECT * FROM " + WRITER_TABLE  + " w " +
            "LEFT JOIN writer_posts wp ON w.id = wp.writer_id " +
            "LEFT JOIN posts p on wp.post_id = p.id " +
            "WHERE w.status = 'ACTIVE' ";

    private static final String GET_BY_ID_QUERY = "SELECT * FROM " + WRITER_TABLE  + " w " +
            "LEFT JOIN writer_posts wp ON w.id = wp.writer_id " +
            "LEFT JOIN posts p on wp.post_id = p.id " +
            "WHERE w.status = 'ACTIVE' AND w.id = ? ";

    private static final String INSERT_QUERY = "INSERT INTO " + WRITER_TABLE  + "( first_name, last_name, status) VALUES ( ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE " + WRITER_TABLE  + " SET  first_name = ?, last_name = ?, status = ? WHERE id = ?";

    private static final String DELETE_QUERY = "UPDATE " + WRITER_TABLE  + " SET status = ? WHERE id = ?";

        @Override
    public Writer getById(Integer integer) {
        Writer writer = null;
            try (PreparedStatement statement = JDBCUtil.getPreparedStatement(GET_BY_ID_QUERY)) {

            statement.setInt(1, integer);
            ResultSet rs = statement.executeQuery();
            Map<Integer,Writer> writerMap = new HashMap<>();

            while (rs.next()){
                int id = rs.getInt("w.id");
                if(!writerMap.containsKey(id)){
                    List<Post> writerPosts = new ArrayList<>();

                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String status = rs.getString("status");

                    writer = Writer.builder()
                            .id(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .status(Status.valueOf(status))
                            .writerPosts(writerPosts)
                            .build();

                    writerMap.put(id, writer);
            }

        }

        return writer;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
}

    @Override
    public List<Writer> getAll() {
        List<Writer> writers = new ArrayList<>();
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(READ_QUERY)) {
            Connection connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);

            ResultSet rs = preparedStatement.executeQuery();
            Map<Integer, Writer> writerMap = new HashMap<>();

            while(rs.next()){
                int id = rs.getInt("w.id");
                if(!writerMap.containsKey(id)){

                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String status = rs.getString("status");

                    Writer writer = Writer.builder()
                            .id(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .status(Status.valueOf(status))
                            .build();

                    writerMap.put(id, writer);
                    writers.add(writer);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return writers;
    }

    @Override
    @SneakyThrows
    public Writer save(Writer writer) {
        try {
            Connection connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {


                preparedStatement.setString(1, writer.getFirstName());
                preparedStatement.setString(2, writer.getLastName());
                preparedStatement.setString(3, writer.getStatus().toString());
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    writer.setId(id);
                }
                try {
                    connection.commit();
                } catch (SQLException throwables) {
                    connection.rollback();
                    throwables.printStackTrace();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return writer;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Writer update(Writer writer) {
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(UPDATE_QUERY)) {
            Connection connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);

            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2,writer.getLastName());
            preparedStatement.setString(3, writer.getStatus().toString());
            preparedStatement.setInt(4, writer.getId());

            preparedStatement.executeUpdate();

            try {
                connection.commit();
            } catch (SQLException throwables) {
                connection.rollback();
                throwables.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return writer;
    }

    @Override
    public void deleteById(Integer integer) {
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(DELETE_QUERY)){
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
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }
}
