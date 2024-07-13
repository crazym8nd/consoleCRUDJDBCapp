package com.vitaly.crudjdbcapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
17-Dec-23
gh /crazym8nd
*/
public class JDBCUtil {

    private JDBCUtil() {
    }

    private static final String DATABASE_URL = "jdbc:mysql://localhost/crudapp";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    private static volatile Connection connection;


    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (JDBCUtil.class) {
                if (connection == null) {
                    connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                }
            }
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

}



