package com.example.blogsaga.utils.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BaseArticle  {
    private String imageUri;
    private String title;
    private String description;
    private String ID;

    public BaseArticle(String imageUri, String title, String description) {
        this.imageUri = imageUri;
        this.title = title;
        this.description = description;
    }

    public BaseArticle(Articles articles,String ID) {
        imageUri = articles.getImageUri();
        title = articles.getTitle();
        description = articles.getDescription();
        this.ID = ID;
    }



    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return ID;
    }
}