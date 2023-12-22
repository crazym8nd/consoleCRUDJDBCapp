package com.vitaly.crudjdbcapp.model;

public class Label {
    private int id;
    private String name;
    private Status status;

    @Override
    public String toString() {
        return "\nLabel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Status=" + status +
                '}';
    }

    public Label() {
    }

    public Label(int id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
