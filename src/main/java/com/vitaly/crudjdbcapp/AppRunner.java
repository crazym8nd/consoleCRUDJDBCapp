package com.vitaly.crudjdbcapp;

import com.vitaly.crudjdbcapp.repository.impls.JDBCPostRepositoryImpl;

public class AppRunner {
    public static void main(String[] args) {
//        MainView mainView = new MainView();
//        mainView.start();

        JDBCPostRepositoryImpl postRepository = new JDBCPostRepositoryImpl();
        System.out.println(postRepository.getAll());
    }
}

