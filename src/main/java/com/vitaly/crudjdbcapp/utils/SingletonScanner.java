package com.vitaly.crudjdbcapp.utils;

import java.util.Scanner;

public class SingletonScanner {
    private static volatile SingletonScanner instance;
    private final Scanner scanner;

    private SingletonScanner() {
        scanner = new Scanner(System.in);
    }

    public static SingletonScanner getInstance() {
        if (instance == null) {
            synchronized (SingletonScanner.class) {
                if (instance == null) {
                    instance = new SingletonScanner();
                }
            }
        }
        return instance;
    }

    public String nextLine() {
        return scanner.nextLine();
    }
}
