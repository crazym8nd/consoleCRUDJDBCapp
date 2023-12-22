package com.vitaly.crudjdbcapp.view;

import java.util.Scanner;

public class MainView {
    private final LabelView labelView = new LabelView();


    private static final String MENU = "Выберете действие:\n" +
            "1. Работа с лейблами\n" +
            "2. Работа с постами\n" +
            "3. Работа с писателями\n" +
            "4. Выход из программы ";

    public void start() {
        try(Scanner scanner = new Scanner(System.in)) {

            boolean isExit = false;
            do {
                System.out.println(MENU);
                String selectedOption = scanner.nextLine();
                switch (selectedOption) {
                    case "1":
                        labelView.show();
                        break;
                    //        case "2":
                    //           postView.show();
                    //           break;
                    //      case "3":
                    //         writerView.show();
                    //            break;
                    case "4":
                        isExit = true;
                        break;
                    default:
                        System.out.println("Если вы не кот, то выберите пункт меню и введите цифру!");
                        break;
                }
            } while (!isExit);
        }
    }
}

