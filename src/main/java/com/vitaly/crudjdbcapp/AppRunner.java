package com.vitaly.crudjdbcapp;

import com.vitaly.crudjdbcapp.utils.SingletonScanner;
import com.vitaly.crudjdbcapp.view.MainMenuRunner;


public class AppRunner {
    public static void main(String[] args) {
        SingletonScanner scanner = SingletonScanner.getInstance();
        MainMenuRunner mainMenuRunner = new MainMenuRunner(scanner);
        mainMenuRunner.start();
    }
}