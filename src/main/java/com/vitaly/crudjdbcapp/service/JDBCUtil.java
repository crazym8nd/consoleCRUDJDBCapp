package com.vitaly.crudjdbcapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
17-Dec-23
gh /crazym8nd
*/
public class JDBCUtil {

    private JDBCUtil() {
        throw new IllegalStateException("Utility class");
    }

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/crudapp";
    static final String USER = "root";
    static final String PASSWORD = "YOUR_PASSWORD";

    public static Connection getConnnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
