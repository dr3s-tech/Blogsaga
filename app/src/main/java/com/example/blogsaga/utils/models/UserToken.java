package com.example.blogsaga.utils.models;

import android.graphics.Bitmap;

public class UserToken {
    private String email, password;

    private Bitmap dpBitMap;
    public UserToken(String email, String password,Bitmap dpBitMap) {
        this.email = email;
        this.password = password;
        this.dpBitMap=dpBitMap;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getDpBitMap() {
        return dpBitMap;
    }

    public String getPassword() {
        return password;
    }
}
