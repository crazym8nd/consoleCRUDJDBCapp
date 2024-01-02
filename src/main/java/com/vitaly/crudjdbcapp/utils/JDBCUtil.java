package com.vitaly.crudjdbcapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
17-Dec-23
gh /crazym8nd
*/
public class JDBCUtil {
    private static JDBCUtil instance;
    private static Connection connection;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/crudapp";
    static final String USER = "root";
    static final String PASSWORD = "6663";

    private JDBCUtil() {
    }

    public static JDBCUtil getInstance(){
        if(instance == null){
            instance = new JDBCUtil();
        }
        return instance;
    }
    public Connection getConnection(){
        if(connection == null){
            try{ connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            connection.setAutoCommit(false);}
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return connection;
    }

}



