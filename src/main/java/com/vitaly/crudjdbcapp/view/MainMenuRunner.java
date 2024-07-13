package com.vitaly.crudjdbcapp.view;

import com.vitaly.crudjdbcapp.utils.SingletonScanner;
import com.vitaly.crudjdbcapp.view.impls.LabelViewImpl;
import com.vitaly.crudjdbcapp.view.impls.PostViewImpl;
import com.vitaly.crudjdbcapp.view.impls.WriterViewImpl;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class MainMenuRunner {
    private final SingletonScanner scanner;
    private final Map<Integer, View> views = new HashMap<>();
    private static final String MENU = """
               Choose action:
               1. Work with labels
               2. Work with posts
               3. Work with writers
               4. Exit application
               """;

    public MainMenuRunner(SingletonScanner scanner) {
        this.scanner = scanner;
        views.put(1, new LabelViewImpl(scanner));
        views.put(2, new PostViewImpl(scanner));
        views.put(3, new WriterViewImpl(scanner));
    }

    public void start() {
        boolean isExit = false;
        do {
            System.out.println(MENU);
            try {
                int selectedOption = Integer.parseInt(scanner.nextLine());
                if (selectedOption == 4) {
                    isExit = true;
                } else {
                    View view = views.get(selectedOption);
                    if (view != null) {
                        view.showMenu();
                    } else {
                        System.out.println("Wrong input. Try again");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong input. Select correct number to continue.");
                scanner.nextLine();
            }
        } while (!isExit);
    }
}
