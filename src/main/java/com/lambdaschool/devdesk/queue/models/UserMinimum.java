package com.lambdaschool.devdesk.queue.models;

public class UserMinimum {
    private String username;
    private String password;

    public UserMinimum(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserMinimum() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
