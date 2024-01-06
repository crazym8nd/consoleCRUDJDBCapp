package com.vitaly.crudjdbcapp;

import com.vitaly.crudjdbcapp.utils.JDBCUtil;
import com.vitaly.crudjdbcapp.view.MainView;

import java.sql.Connection;
import java.sql.SQLException;


public class AppRunner {
    public static void main(String[] args) throws SQLException {

        MainView mainView = new MainView();
        mainView.start();



    }
}

