package com.vitaly.crudjdbcapp.view;

import com.vitaly.crudjdbcapp.controller.PostController;
import com.vitaly.crudjdbcapp.model.Post;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/*
18-Dec-23
gh /crazym8nd
*/
public class PostView {
    private final PostController postController = new PostController();
    private final Scanner scanner = new Scanner(System.in);

    private static final String MENUPOST = "Выберете действие:\n" +
            " 1. Создать пост\n" +
            " 2. Редактировать пост\n" +
            " 3. Удалить пост\n" +
            " 4. Вывести список постов\n" +
            " 5. Выход в главное меню";
    private static final String PRINT_POST_LIST = "Список постов:\n";
    private static final String CREATE_POST_MSG = "Создание поста.\n" + "Введите content: ";
    private static final String EDIT_POST_MSG = "Редактирование поста. Введите ID:";
    private static final String DELETE_POST_MSG = "Удаление поста.Введите ID:";

    public void show() {
        boolean isExit = false;
        do {
            System.out.println(MENUPOST);
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    createPost();
                    break;
                case "2":
                    editPost();
                    break;
                case "3":
                    deletePost();
                    break;
                case "4":
                    readPosts();
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

    public void createPost() {
        System.out.println(CREATE_POST_MSG);
        String content = scanner.nextLine();
        try {
            Post createdPost = postController.createPost(content);
            System.out.println("Пост создан:" + createdPost);
        } catch (Exception e) {
            System.out.println("Ошибка при создании поста");
        }
    }

    public void editPost() {
        readPosts();
        System.out.println(EDIT_POST_MSG);
        try {
            Integer id = Integer.parseInt(scanner.nextLine());
            Post postToUpdate = postController.getById(id);
            System.out.println("Введите новый content:");
            String content = scanner.nextLine();
            postToUpdate.setContent(content);
            postController.update(postToUpdate);
            System.out.println("Изменения сохранены");
        } catch (Exception e) {
            System.out.println("Не получилось изменить пост");
        }
    }

    public void deletePost() {
        readPosts();
        System.out.println(DELETE_POST_MSG);
        Integer id = scanner.nextInt();
        try {
            postController.deleteById(id);
            System.out.println("Пост удален");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении поста");
        }
    }

    public void readPosts() {
        List<Post> posts = null;
        try {
            posts = postController.getAll();
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка постов");
        }
        System.out.println(PRINT_POST_LIST);
        if (posts != null) {
            posts.sort(Comparator.comparing(Post::getId));
            for (Post p : posts) {
                System.out.println(p.getId() + " " + p.getContent());
            }
        }
    }
}
