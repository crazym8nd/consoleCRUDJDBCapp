package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;

import java.sql.*;
import java.util.*;

public class JDBCLabelRepositoryImpl implements LabelRepository {
    private static final String LABELS_TABLE = "labels";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM " + LABELS_TABLE + " WHERE id = ?";
    private static final String READ_QUERY = "SELECT * FROM " + LABELS_TABLE + " WHERE status = 'ACTIVE'";
    private static final String INSERT_QUERY = "INSERT INTO " + LABELS_TABLE + "(name, status) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + LABELS_TABLE + " SET name = ?, status = ? WHERE id = ?";
    private static final String DELETE_QUERY = "UPDATE " + LABELS_TABLE + " SET status = ? WHERE id = ?";

    @Override
    public Label getById(Integer integer) {
        Label label = null;
        try(PreparedStatement statement = JDBCUtil.getPreparedStatement(GET_BY_ID_QUERY)) {
            statement.setInt(1, integer);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                label = new Label(id, name, (Status.valueOf(status)));
            }
            if (label != null){
                return label;
            } else {
                return new Label(-1,null,null);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getSQLState());
        }
    }
    @Override
    public List<Label> getAll() {
        List<Label> labels = new ArrayList<>();
        try(PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(READ_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                labels.add(new Label(id, name, (Status.valueOf(status))));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e.getSQLState());
        }

        return labels;
    }

    @Override
    public Label save(Label label) {
        if (label != null) {
            try {
                Connection connection = JDBCUtil.getConnection();
                connection.setAutoCommit(false);
                try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {


                    preparedStatement.setString(1, label.getName());
                    preparedStatement.setString(2, label.getStatus().toString());
                    preparedStatement.executeUpdate();

                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        label.setId(id);
                    }
                    try {
                        connection.commit();
                    } catch (SQLException e) {
                        connection.rollback();
                        throw new RuntimeException(e.getSQLState());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getSQLState());
            }

            return label;
        } else {
            return new Label(-1, null, null);
        }
    }
    @Override
    public Label update(Label label) {
        if (label != null) {
            try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(UPDATE_QUERY)) {
                Connection connection = JDBCUtil.getConnection();
                connection.setAutoCommit(false);

                preparedStatement.setString(1, label.getName());
                preparedStatement.setString(2, label.getStatus().toString());
                preparedStatement.setInt(3, label.getId());
                preparedStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e.getSQLState());
            }
            return label;
        } else {
            return new Label(-1, null, null);
        }
    }

    @Override
    public void deleteById(Integer integer) {
        if(getById(integer).getStatus() != null)
        {
        try(PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(DELETE_QUERY)){
            Connection connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);

            preparedStatement.setString(1, Status.DELETED.toString());
            preparedStatement.setInt(2, integer);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    } else {
        throw new NoSuchElementException("Такого лейбла не существует");
        }
    }
}
