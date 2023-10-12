package com.example.blogsaga.utils.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class User {
    private static User user;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private User() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
    }
    public static User getInstance(){
        if(null==user) return new User();
        return user;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
