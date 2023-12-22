package com.vitaly.crudjdbcapp.model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {

    private Integer id;
    private String content;

    private String created;

    private String updated;
    private PostStatus postStatus;
    private List<Label> postLabels;

    public Post(Integer id, String content, String created, String updated,List<Label> postLabels, PostStatus postStatus) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.postLabels = postLabels;
        this.postStatus = postStatus;
    }


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

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
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

    @Override
    public String toString() {
        return "\nPost{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", postStatus=" + postStatus +
                ", postLabels=" + postLabels +
                '}';
    }

}

