package com.vitaly.crudjdbcapp.view;

import com.vitaly.crudjdbcapp.controller.PostController;
import com.vitaly.crudjdbcapp.controller.WriterController;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.Writer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/*
03-Jan-24
gh /crazym8nd
*/
public class WriterView {

    private final WriterController writerController = new WriterController();

    private final Scanner scanner = new Scanner(System.in);

    private static final String MENUWRITER = "Выберете действие:\n" +
            " 1. Создать писателя\n" +
            " 2. Редактировать писателя\n" +
            " 3. Удалить писателя\n" +
            " 4. Вывести список писателей\n" +
            " 5. Выход в главное меню";

    private static final String PRINT_WRITER_LIST = "Список писателей:\n";
    private static final String CREATE_WRITER_MSG = "Создание писателя.\n" + "Введите имя: ";
    private static final String EDIT_WRITER_MSG = "Редактирование писателя. Введите ID:";
    private static final String DELETE_WRITER_MSG = "Удаление писателя.Введите ID:";

    public void show() {
        boolean isExit = false;
        do {
            System.out.println(MENUWRITER);
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    createWriter();
                    break;
                case "2":
                    editWriter();
                    break;
                case "3":
                    deleteWriter();
                    break;
                case "4":
                    readWriters();
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
    public void createWriter(){
        List<Post> writerPosts = new ArrayList<>();
        PostController postController = new PostController();

        System.out.println(CREATE_WRITER_MSG);
        String firstName = scanner.nextLine();
        System.out.println("Введите фамилию:");
        String lastName = scanner.nextLine();

        PostView postView = new PostView();
        postView.readPosts();
        System.out.println("Введите ID поста для доабвления к писателю:");
        Integer postID = scanner.nextInt();
        writerPosts.add(postController.getById(postID));
        try{
            Writer createdWriter = writerController.createWriter(firstName, lastName, writerPosts);
            System.out.println("Писатель создан:" + createdWriter);
        } catch (Exception e) {
            System.out.println("Ошибка при создании писателя");
        }
    }

    public void editWriter(){
        PostController postController = new PostController();
        readWriters();
        System.out.println(EDIT_WRITER_MSG);
        Integer id = Integer.parseInt(scanner.nextLine());
        try{
            Writer writerToUpdate = writerController.getById(id);
            System.out.println("Введите новое имя");
            String firstName = scanner.nextLine();
            writerToUpdate.setFirstName(firstName);
            System.out.println("Введите новую фамилию");
            String lastName = scanner.nextLine();
            writerToUpdate.setLastName(lastName);
            writerController.update(writerToUpdate);
            System.out.println("Писатель обновлен");
        } catch (Exception e){
            System.out.println("Ошибка при обновлении писателя");
        }
    }
    public void deleteWriter() {
        readWriters();
        System.out.println(DELETE_WRITER_MSG);
        Integer id = scanner.nextInt();
        try{
            writerController.deleteById(id);
            System.out.println("Писатель удален");
        } catch (Exception e){
            System.out.println("Ошибка при удалении писателя");
        }
    }
    public void readWriters(){
        List<Writer> writers = null;
        try{
            writers =writerController.getAll();
        } catch (Exception e){
            System.out.println("Ошибка при получении списка писателей");
        }
        System.out.println(PRINT_WRITER_LIST);
        if(writers != null){
            writers.sort(Comparator.comparing(Writer::getId));
            for (Writer w : writers){
                System.out.println(w.getId() + " " + w.getFirstName() + " " + w.getLastName());
            }
        }
    }
}
