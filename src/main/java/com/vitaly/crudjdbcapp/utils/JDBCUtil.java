package com.vitaly.crudjdbcapp.utils;

import java.sql.*;

/*
17-Dec-23
gh /crazym8nd
*/
public class JDBCUtil {

    private JDBCUtil(){}
    private static Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql://localhost/crudapp";
    private static final String USER = "root";
    private static final String PASSWORD = "6663";


    public static Connection getConnection(){
        if(connection == null){
            try { connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            } catch (SQLException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException{

        return getConnection().prepareStatement(sql);
    }

}



