package com.example.blogsaga.utils.models;

import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationModel {
    private String userId, text, postid;
    private boolean ispost;

    public NotificationModel() {
    }

    public NotificationModel(String userId, String text, String postid, boolean ispost) {
        this.userId = userId;
        this.text = text;
        this.postid = postid;
        this.ispost = ispost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}

