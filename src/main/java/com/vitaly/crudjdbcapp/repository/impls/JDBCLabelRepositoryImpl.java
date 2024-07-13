package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.exceptions.NoLabelsInDbException;
import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;

import java.sql.*;
import java.util.*;

public class JDBCLabelRepositoryImpl implements LabelRepository {
    private final Connection connection;

    private static final String LABELS_TABLE = "labels";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM " + LABELS_TABLE + " WHERE id = ?";
    private static final String READ_QUERY = "SELECT * FROM " + LABELS_TABLE + " WHERE status = 'ACTIVE'";
    private static final String INSERT_QUERY = "INSERT INTO " + LABELS_TABLE + "(name, status) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + LABELS_TABLE + " SET name = ?, status = ? WHERE id = ?";
    private static final String DELETE_QUERY = "UPDATE " + LABELS_TABLE + " SET status = ? WHERE id = ?";

    public JDBCLabelRepositoryImpl() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Label> getById(Integer integer) {
        try (PreparedStatement statement = JDBCUtil.getPreparedStatement(GET_BY_ID_QUERY)) {
            statement.setInt(1, integer);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                Label label = new Label(id, name, Status.valueOf(status));
                return Optional.of(label);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getSQLState());
        }
        return Optional.empty();
    }

    @Override
    public List<Label> getAll() {
        List<Label> labels = new ArrayList<>();
        try (PreparedStatement preparedStatement = JDBCUtil.getPreparedStatement(READ_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                throw new NoLabelsInDbException("No labels found in the database");
            }

            do {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                labels.add(new Label(id, name, Status.valueOf(status)));
            } while (rs.next());

            return labels;
        } catch (NoLabelsInDbException e) {
            System.out.println("No labels found in the database");
            return Collections.emptyList();
        } catch (SQLException e) {
            throw new RuntimeException(e.getSQLState());
        }
    }

    @Override
    public Label save(Label label) {
        if (label != null) {
            try {
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
                        connection.setAutoCommit(true);
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
    public void update(Label label) {
        try (PreparedStatement statement = JDBCUtil.getPreparedStatement(UPDATE_QUERY)) {
            statement.setString(1, label.getName());
            statement.setString(2, label.getStatus().toString());
            statement.setInt(3, label.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating label failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer integer) {
        Optional<Label> labelOpt = getById(integer);
        if (labelOpt.isPresent() && labelOpt.get().getStatus() != null) {
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
            throw new NoSuchElementException("Label with such id is not existing");
        }
    }
}
