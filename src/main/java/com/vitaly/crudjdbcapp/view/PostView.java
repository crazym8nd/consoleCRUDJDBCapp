package com.vitaly.crudjdbcapp.view;

import com.vitaly.crudjdbcapp.controller.LabelController;
import com.vitaly.crudjdbcapp.controller.PostController;
import com.vitaly.crudjdbcapp.controller.WriterController;
import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.Writer;

import java.text.SimpleDateFormat;
import java.util.*;

/*
18-Dec-23
gh /crazym8nd
*/
public class PostView {
    private final PostController postController = new PostController();
    private final LabelController labelController = new LabelController();
    private final WriterController writerController = new WriterController();
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
        List<Label> postLabels = new ArrayList<>();

        System.out.println(CREATE_POST_MSG);
        String content = scanner.nextLine();

        List<Label> labels = labelController.getAll();
        if (labels != null) {
            labels.sort(Comparator.comparing(Label::getId));
            for (Label l : labels) {
                System.out.println(l.getId() + " " + l.getName());
            }
        }
        System.out.println("Введите ID лейбла для добавления к посту:");

        Integer labelID = Integer.parseInt(scanner.nextLine());
        postLabels.add(labelController.getById(labelID));


        List<Writer> writers = writerController.getAll();
        if (writers != null) {
            writers.sort(Comparator.comparing(Writer::getId));
            for (Writer w : writers) {
                System.out.println(w.getId() + " " + w.getFirstName());
            }
        }
        System.out.println("Введите ID автора:");
        Integer writerId = Integer.parseInt(scanner.nextLine());

        try {
            Post createdPost = postController.createPost(content, postLabels, writerId);
            System.out.println("Пост создан:" + createdPost);
        } catch (Exception e) {
            System.out.println("Ошибка при создании поста");
        }
    }

    public void editPost() {
        readPosts();
        System.out.println(EDIT_POST_MSG);
        Integer id = Integer.parseInt(scanner.nextLine());
        try {
            Post postToUpdate = postController.getById(id);
            postToUpdate.setUpdated(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
            System.out.println("Введите новый content:");
            String content = scanner.nextLine();
            postToUpdate.setContent(content);

            List<Label> labels = labelController.getAll();
            if (labels != null) {
                labels.sort(Comparator.comparing(Label::getId));
                for (Label l : labels) {
                    System.out.println(l.getId() + " " + l.getName());
                }
            }
            System.out.println("Введите ID лейбла для добавления к посту:");
            Integer labelID = Integer.parseInt(scanner.nextLine());

            List<Label> labelstoAdd = new ArrayList<>();
            labelstoAdd.add(labelController.getById(labelID));
            postToUpdate.setPostLabels(labelstoAdd);

            List<Writer> writers = writerController.getAll();
            if (writers != null) {
                writers.sort(Comparator.comparing(Writer::getId));
                for (Writer w : writers) {
                    System.out.println(w.getId() + " " + w.getFirstName());
                }
            }

            System.out.println("Введите ID автора:");
            Integer writerId = Integer.parseInt(scanner.nextLine());
            postToUpdate.setWriterId(writerId);

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
