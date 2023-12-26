package com.vitaly.crudjdbcapp.repository.impls;

import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Status;
import com.vitaly.crudjdbcapp.repository.LabelRepository;
import com.vitaly.crudjdbcapp.utils.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCLabelRepositoryImpl implements LabelRepository {
    private static final String LABELS_TABLE = "labels";
    private static final String READ_QUERY = "SELECT * FROM " + LABELS_TABLE + " WHERE status = 'ACTIVE'";
    private static final String INSERT_QUERY = "INSERT INTO " + LABELS_TABLE + "(name, status) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE " + LABELS_TABLE + " SET name = ?, status = ? WHERE id = ?";
    private static final String DELETE_QUERY = "UPDATE " + LABELS_TABLE + " SET status = ? WHERE id = ?";

    private static List<Label> getLabelsData(String query) {
        List<Label> labels = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                labels.add(new Label(id, name, (Status.valueOf(status))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return labels;
    }

    @Override
    public Label getById(Integer integer) {
        List<Label> labels = getLabelsData(READ_QUERY);
        return labels.stream().
                filter(l -> l.getId().equals(integer)).
                findFirst().orElse(null);
    }

    @Override
    public List<Label> getAll() {
        return getLabelsData(READ_QUERY);
    }

    @Override
    public Label save(Label label) {

        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

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
            } catch (SQLException throwables) {
                connection.rollback();
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return label;
    }

    @Override
    public Label update(Label label) {
        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {

            preparedStatement.setString(1, label.getName());
            preparedStatement.setString(2, label.getStatus().toString());
            preparedStatement.setInt(3, label.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return label;
    }

    @Override
    public void deleteById(Integer integer) {
        try (Connection connection = JDBCUtil.getConnnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {

            preparedStatement.setString(1, Status.DELETED.toString());
            preparedStatement.setInt(2, integer);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
