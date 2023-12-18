package com.vitaly.crudjdbcapp.model;

public class Label {
    private int id;
    private String name;
    private PostStatus postStatus;

    @Override
    public String toString() {
        return "\nLabel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", postStatus=" + postStatus +
                '}';
    }

    public Label() {
    }

    public Label(int id, String name, PostStatus postStatus) {
        this.id = id;
        this.name = name;
        this.postStatus = postStatus;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setId(int id) {
        this.id = id;
    }

}
