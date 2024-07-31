package com.cs2340aG49.greenPlate.ui.model;

public class User {
    private String username;
    // cm, kg?
    private double height;
    private double weight;
    private String gender; // Usually a boolean, but

    public User() {

    }

    public User(String username, double height, double weight, String gender) {
        this.username = username;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" + "userName='" + username + '\''
                + ", height=" + height + ", weight=" + weight
                + ", gender='" + gender + '\'' + '}';
    }
}
