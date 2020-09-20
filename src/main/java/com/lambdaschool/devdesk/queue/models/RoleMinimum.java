package com.lambdaschool.devdesk.queue.models;


import javax.validation.constraints.NotNull;

public class RoleMinimum {
    private long id;
    @NotNull
    private String name;

    public RoleMinimum() {
    }

    public RoleMinimum(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
