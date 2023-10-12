package com.example.blogsaga.utils.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Articles implements Parcelable {
    private String imageUri;
    private String title;
    private String description;
    private String ID;
    private byte[] imageBytes;
    public Articles(){
        System.out.println("default");
    }

    public Articles(String imageUri, String title, String description, String ID, byte[] imageBytes) {
        this.imageUri = imageUri;
        this.title = title;
        this.description = description;
        this.ID = ID;
        this.imageBytes = imageBytes;
    }

    public Articles(String imageUri, String title, String description) {
        this.imageUri = imageUri;
        this.title = title;
        this.description = description;
        System.out.println("default 1"+imageUri);
    }

    public Articles(byte[] imageBytes, String title, String description) {
        this.imageBytes = imageBytes;
        this.title = title;
        this.description = description;
        System.out.println("default 2");

    }

    protected Articles(Parcel in) {
        imageUri = in.readString();
        title = in.readString();
        description = in.readString();
        ID = in.readString();
        imageBytes = in.createByteArray();
    }

    public static final Creator<Articles> CREATOR = new Creator<Articles>() {
        @Override
        public Articles createFromParcel(Parcel in) {
            return new Articles(in);
        }

        @Override
        public Articles[] newArray(int size) {
            return new Articles[size];
        }
    };

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
    public void setId(String ID){
        this.ID=ID;
    }
    public String getId() {
        return ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(imageUri);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(ID);
        parcel.writeByteArray(imageBytes);
    }
}


