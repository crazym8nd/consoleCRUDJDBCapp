package com.vitaly.crudjdbcapp.model;

import java.util.List;

public class Writer {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<Post> writerPosts;
    private PostStatus postStatus;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PostStatus getStatus() {
        return postStatus;
    }

    public void setStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWriterPosts(List<Post> writerPosts) {
        this.writerPosts = writerPosts;
    }

    public List<Post> getWriterPosts() {
        return writerPosts;
    }


}
