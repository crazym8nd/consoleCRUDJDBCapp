package com.vitaly.crudjdbcapp.view.impls;

import com.vitaly.crudjdbcapp.controller.WriterController;
import com.vitaly.crudjdbcapp.model.Writer;
import com.vitaly.crudjdbcapp.utils.SingletonScanner;
import com.vitaly.crudjdbcapp.view.View;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
03-Jan-24
gh /crazym8nd
*/
public class WriterViewImpl implements View {

    private final WriterController writerController = new WriterController();

    private final SingletonScanner scanner;

    private static final String MENU_WRITER = """
                      Choose action
                                         1. Create writer
                                         2. Edit writer
                                         3. Delete writer
                                         4. Show list of all writers
                                         5. Exit to main menu
                     """;

    private static final String PRINT_WRITER_LIST = "List of writers:\n";
    private static final String CREATE_WRITER_MSG = "Creating writer.\n" + "Enter name: ";
    private static final String EDIT_WRITER_MSG = "Editing writer. Enter ID:";
    private static final String DELETE_WRITER_MSG = "Deleting writer.Enter ID:";

    public WriterViewImpl(SingletonScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void showMenu() {
        boolean isExit = false;
        do {
            System.out.println(MENU_WRITER);
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

        System.out.println(CREATE_WRITER_MSG);
        String firstName = scanner.nextLine();
        System.out.println("Enter last name:");
        String lastName = scanner.nextLine();

        try {
            Writer createdWriter = writerController.createWriter(firstName, lastName);
            System.out.println("Writer created:" + createdWriter);
        } catch (Exception e) {
            System.out.println("Error: Unable to create writer");
        }
    }

    @Override
    public void edit() {
        list();
        System.out.println(EDIT_WRITER_MSG);
        Integer id = Integer.parseInt(scanner.nextLine());
        try {
            Optional<Writer> optionalWriterToUpdate = writerController.getById(id);

            if (optionalWriterToUpdate.isPresent()) {
                Writer writerToUpdate = optionalWriterToUpdate.get();
                System.out.println("Enter new name:");
                String firstName = scanner.nextLine();
                writerToUpdate.setFirstName(firstName);
                System.out.println("Enter new last name:");
                String lastName = scanner.nextLine();
                writerToUpdate.setLastName(lastName);
                writerController.update(writerToUpdate);
                System.out.println("Writer successfully updated!");
            } else {
                System.out.println("Writer with ID" + id + " not found");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to update writer");
        }
    }

    @Override
    public void delete() {
        list();
        System.out.println(DELETE_WRITER_MSG);
        Integer id = Integer.valueOf(scanner.nextLine());
        try {
            writerController.deleteById(id);
            System.out.println("Writer deleted.");
        } catch (Exception e) {
            System.out.println("Error: Unable to delete writer");
        }
    }

    @Override
    public void list() {
        List<Writer> writers = null;
        try {
            writers = writerController.getAll();
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve list of  writers");
        }
        System.out.println(PRINT_WRITER_LIST);
        if (writers != null) {
            writers.sort(Comparator.comparing(Writer::getId));
            for (Writer w : writers) {
                System.out.println(w.getId() + " " + w.getFirstName() + " " + w.getLastName());
            }
        }
    }
}
