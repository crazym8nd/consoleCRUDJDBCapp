package com.vitaly.crudjdbcapp.view;

import com.vitaly.crudjdbcapp.controller.LabelController;
import com.vitaly.crudjdbcapp.model.Label;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
18-Dec-23
gh /crazym8nd
*/
public class LabelView {
    private final LabelController labelController = new LabelController();
    private final Scanner scanner = new Scanner(System.in);

    private static final String MENULABEL = "Выберете действие:\n" +
            " 1. Создать лейбл\n" +
            " 2. Редактировать лейбл\n" +
            " 3. Удалить лейбл\n" +
            " 4. Вывести список лейблов\n" +
            " 5. Выход в главное меню";
    private static final String PRINT_LABEL_LIST = "Список лейблов:\n";
    private static final String CREATE_LABEL_MSG = "Создание лейбла.\n" + "Введите название: ";
    private static final String EDIT_LABEL_MSG = "Редактирование лейбла. Введите ID:";
    private static final String DELETE_LABEL_MSG = "Удаление лейбла.Введите ID:";

    public void show() {
        boolean isExit = false;
        do {
            System.out.println(MENULABEL);
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    createLabel();
                    break;
                case "2":
                    editLabel();
                    break;
                case "3":
                    deleteLabel();
                    break;
                case "4":
                    readLabels();
                    break;
                case "5":
                    isExit = true;
                    break;
                default:
                    System.out.println("Выберите пункт меню.");
                    break;
            }
        } while (!isExit);
    }

    public void createLabel() {
        System.out.println(CREATE_LABEL_MSG);
        String name = scanner.nextLine();
        try {
            Label createdLabel = labelController.createLabel(name);
            System.out.println("Лейбл создан: " + createdLabel);
        } catch (Exception e) {
            System.out.println("Ошибка при создании лейбла");
        }
    }

    public void editLabel() {
        readLabels();
        System.out.println(EDIT_LABEL_MSG);
        try {
            Integer id = Integer.parseInt(scanner.nextLine());
            Label labelToUpdate = labelController.getById(id);
            if (labelToUpdate.getId() == -1) {
                System.out.println("Лейбла с ID " + id + " не существует");
                return;
            }
            System.out.println("Введите новое имя:");
            String name = scanner.nextLine();
            labelToUpdate.setName(name);
            labelController.update(labelToUpdate);
            System.out.println("Изменения сохранены");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Не получилось изменить лейбл");
        }
    }

    public void deleteLabel() {
        readLabels();
        System.out.println(DELETE_LABEL_MSG);
        Integer id = Integer.parseInt(scanner.nextLine());
        try {
            labelController.deleteById(id);
            System.out.println("Лейбл удален");
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка при удалении лейбла");
        }
    }

    public void readLabels() {
        List<Label> labels = null;
        try {
            labels = labelController.getAll();
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка лейблов");
        }
        System.out.println(PRINT_LABEL_LIST);
        if (labels != null) {
            labels.sort(Comparator.comparing(Label::getId));
            for (Label l : labels) {
                System.out.println(l.getId() + " " + l.getName());
            }
        }
    }
}
