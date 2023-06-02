package com.example.task61d.model;

import android.graphics.Bitmap;

public class User {
    private String username, fullName, password, phoneNumber;
    private Bitmap userImage;

    public User(String username, String fullName, String password, String phoneNumber, Bitmap userImage) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userImage = userImage;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }
}
