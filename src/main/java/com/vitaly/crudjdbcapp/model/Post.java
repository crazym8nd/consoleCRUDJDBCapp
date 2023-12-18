package com.vitaly.crudjdbcapp.model;

import java.util.List;

public class Post {
    private Integer id;
    private String content;

    private String created;

    private String updated;
    private PostStatus postStatus;
    private List<Label> postLabels;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostStatus getStatus() {
        return postStatus;
    }

    public void setStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public List<Label> getPostLabels() {
        return postLabels;
    }

    public void setPostLabels(List<Label> postLabels) {
        this.postLabels = postLabels;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

}

