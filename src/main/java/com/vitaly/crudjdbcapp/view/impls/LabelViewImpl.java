package com.vitaly.crudjdbcapp.view.impls;

import com.vitaly.crudjdbcapp.controller.LabelController;
import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.utils.SingletonScanner;
import com.vitaly.crudjdbcapp.view.View;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/*
18-Dec-23
gh /crazym8nd
*/
public class LabelViewImpl implements View {
    private final LabelController labelController = new LabelController();
    private final SingletonScanner scanner;

    public LabelViewImpl(SingletonScanner scanner) {
        this.scanner = scanner;
    }


    private static final String MENU_LABEL = """
                                         Choose action
                                         1. Create label
                                         2. Edit label
                                         3. Delete label
                                         4. Show list of all labels
                                         5. Exit to main menu
                                         """;
    private static final String PRINT_LABEL_LIST = "List of labels:\n";
    private static final String CREATE_LABEL_MSG = "Creating label.\n" + "Enter name: ";
    private static final String EDIT_LABEL_MSG = "Editing label. Enter ID:";
    private static final String DELETE_LABEL_MSG = "Deleting label.Enter ID:";

    @Override
    public void showMenu() {
        boolean isExit = false;
        do {
            System.out.println(MENU_LABEL);
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    create();
                    break;
                case "2":
                    edit();
                    break;
                case "3":
                    delete();
                    break;
                case "4":
                    list();
                    break;
                case "5":
                    isExit = true;
                    break;
                default:
                    System.out.println("Choose action.");
                    break;
            }
        } while (!isExit);
    }

    @Override
    public void create() {
        System.out.println(CREATE_LABEL_MSG);
        String name = getUserInput();
        try {
            Label createdLabel = labelController.createLabel(name);
            System.out.println("Label created: " + createdLabel);
        } catch (Exception e) {
            System.out.println("Error during label creation");
        }
    }

    @Override
    public void edit() {
        list();
        System.out.println(EDIT_LABEL_MSG);
        getIntInput().ifPresentOrElse(id -> labelController.getById(id).ifPresentOrElse(labelToUpdate -> {
            System.out.println("Enter new name:");
            String name = getUserInput();
            labelToUpdate.setName(name);
            labelController.update(labelToUpdate);
            System.out.println("Label successfully updated! ");
        }, () -> System.out.println("Label with ID " + id + " is not existing")), () -> System.out.println("Wrong ID"));
    }

    @Override
    public void delete() {
        list();
        System.out.println(DELETE_LABEL_MSG);
        Integer id = Integer.parseInt(scanner.nextLine());
        try {
            labelController.deleteById(id);
            System.out.println("Label deleted");
        } catch (NoSuchElementException e) {
            System.out.println("Error:Unable to delete label, " + e.getMessage());
        }
    }

    @Override
    public void list() {
        List<Label> labels = null;
        try {
            labels = labelController.getAll();
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve the list of labels. ");
        }
        System.out.println(PRINT_LABEL_LIST);
        if (labels != null) {
            labels.sort(Comparator.comparing(Label::getId));
            for (Label l : labels) {
                System.out.println(l.getId() + " " + l.getName());
            }
        }
    }

    private String getUserInput() {
        return scanner.nextLine().trim();
    }

    private Optional<Integer> getIntInput() {
        String input = getUserInput();
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
