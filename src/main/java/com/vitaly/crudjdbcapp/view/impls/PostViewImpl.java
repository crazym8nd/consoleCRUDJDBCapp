package com.vitaly.crudjdbcapp.view.impls;

import com.vitaly.crudjdbcapp.controller.LabelController;
import com.vitaly.crudjdbcapp.controller.PostController;
import com.vitaly.crudjdbcapp.controller.WriterController;
import com.vitaly.crudjdbcapp.model.Label;
import com.vitaly.crudjdbcapp.model.Post;
import com.vitaly.crudjdbcapp.model.Writer;
import com.vitaly.crudjdbcapp.utils.SingletonScanner;
import com.vitaly.crudjdbcapp.view.View;

import java.text.SimpleDateFormat;
import java.util.*;

/*
18-Dec-23
gh /crazym8nd
*/
public class PostViewImpl implements View {
    private final PostController postController = new PostController();
    private final LabelController labelController = new LabelController();
    private final WriterController writerController = new WriterController();
    private final SingletonScanner scanner;

    private static final String MENU_POST = """
                                         Choose action
                                         1. Create post
                                         2. Edit post
                                         3. Delete post
                                         4. Show list of all posts
                                         5. Exit to main menu
                   """;
    private static final String PRINT_POST_LIST = "List of posts:\n";
    private static final String CREATE_POST_MSG = "Creating post.\n" + "Enter content: ";
    private static final String EDIT_POST_MSG = "Editing post. Enter ID:";
    private static final String DELETE_POST_MSG = "Deleting post. Enter ID:";

    public PostViewImpl(SingletonScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void showMenu() {
        boolean isExit = false;
        do {
            System.out.println(MENU_POST);
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
        System.out.println("Enter the label ID to add to the post:");

        Integer labelId = Integer.parseInt(scanner.nextLine());
        labelController.getById(labelId).ifPresent(postLabels::add);

        List<Writer> writers = writerController.getAll();
        if (writers != null) {
            writers.sort(Comparator.comparing(Writer::getId));
            for (Writer w : writers) {
                System.out.println(w.getId() + " " + w.getFirstName());
            }
        }
        System.out.println("Enter the author ID:");
        Integer writerId = Integer.parseInt(scanner.nextLine());

        boolean isLabelExists = Objects.requireNonNull(labels).stream().anyMatch(label -> label.getId().equals(labelId));
        if (!isLabelExists) {
            System.out.println("Invalid label ID. Please try again.");
            return;
        }
        boolean isWriterExists = Objects.requireNonNull(writers).stream().anyMatch(w -> w.getId().equals(writerId));
        if (!isWriterExists) {
            System.out.println("Invalid writer ID. Please try again.");
            return;
        }

        try {
            Post createdPost = postController.createPost(content, postLabels, writerId);
            System.out.println("Post created: " + createdPost);
        } catch (Exception e) {
            System.out.println("Error creating the post");
        }
    }

    @Override
    public void edit() {
        list();
        System.out.println(EDIT_POST_MSG);
        Integer id = readId();
        try {
            Optional<Post> postToUpdateOpt = postController.getById(id);
            if (postToUpdateOpt.isPresent()) {
                Post postToUpdate = postToUpdateOpt.get();
                postToUpdate.setUpdated(
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                                .format(Calendar.getInstance().getTime())
                );
                String content = readContent();
                postToUpdate.setContent(content);

                List<Label> labels = labelController.getAll();
                if (labels != null) {
                    labels.sort(Comparator.comparing(Label::getId));
                    printLabels(labels);
                }
                Integer labelID = readLabelId();
                Optional<Label> labelOpt = labelController.getById(labelID);
                labelOpt.ifPresent(label -> postToUpdate.setPostLabels(List.of(label)));

                List<Writer> writers = writerController.getAll();
                if (writers != null) {
                    writers.sort(Comparator.comparing(Writer::getId));
                    printWriters(writers);
                }

                Integer writerId = readWriterId();
                postToUpdate.setWriterId(writerId);

                postController.update(postToUpdate);
                System.out.println("Changes successfully saved!");
            } else {
                System.out.println("Post is not found");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to update post.");
        }
    }

    private Integer readId() {
        System.out.println("Enter post ID:");
        return Integer.parseInt(scanner.nextLine());
    }

    private String readContent() {
        System.out.println("Enter new content:");
        return scanner.nextLine();
    }

    private Integer readLabelId() {
        System.out.println("Enter label id to add to the post:");
        return Integer.parseInt(scanner.nextLine());
    }

    private Integer readWriterId() {
        System.out.println("Enter writer ID:");
        return Integer.parseInt(scanner.nextLine());
    }

    private void printLabels(List<Label> labels) {
        for (Label l : labels) {
            System.out.println(l.getId() + " " + l.getName());
        }
    }

    private void printWriters(List<Writer> writers) {
        for (Writer w : writers) {
            System.out.println(w.getId() + " " + w.getFirstName());
        }
    }

    @Override
    public void delete() {
        list();
        System.out.println(DELETE_POST_MSG);
        Integer id = Integer.valueOf(scanner.nextLine());
        try {
            postController.deleteById(id);
            System.out.println("Post deleted.");
        } catch (Exception e) {
            System.out.println("Error: Unable to delete writer");
        }
    }

    @Override
    public void list() {
        List<Post> posts = null;
        try {
            posts = postController.getAll();
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve list of writers");
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
