package com.cs2340aG49.greenPlate.ui.model;

public class Account {
    private String username;
    private String password;

    public Account() {
        // required for firebase

    };
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
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
