package com.example.blogsaga.utils.models;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserTokens {

    private final FirebaseAuth auth;
    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;

    private final StorageReference imageReference;
    private static UserTokens instance;
    private final FirebaseStorage firebaseStorage;

    private UserTokens() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        imageReference = firebaseStorage.getReference().child("Users/");
    }

    public static UserTokens getInstance() {
        if (instance == null) instance = new UserTokens();
        return instance;
    }

    public FirebaseDatabase getDatabase() {
        return instance.database;
    }

    public FirebaseAuth getAuth() {
        return instance.auth;
    }

    public DatabaseReference getDatabaseReference() {
        return instance.databaseReference;
    }

    public StorageReference getImageReference() {
        return instance.imageReference;
    }

    public FirebaseStorage getFirebaseStorage() {
        return instance.firebaseStorage;
    }
}