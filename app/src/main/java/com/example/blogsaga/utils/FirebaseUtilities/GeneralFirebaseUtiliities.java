package com.example.blogsaga.utils.FirebaseUtilities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.blogsaga.utils.callbacks.GeneralCallbacks;
import com.example.blogsaga.utils.models.User;
import com.example.blogsaga.utils.models.UserToken;
import com.example.blogsaga.utils.models.userDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class GeneralFirebaseUtiliities {
    private static User user = User.getInstance();

    public static void registerUser(final UserToken token, final GeneralCallbacks callbacks) {
        FirebaseAuth auth = user.getAuth();
        final String email = token.getEmail();
        final String password = token.getPassword();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                Log.i("Create User ", "Completed");
                                sendVerificationEmail(user, token, callbacks);
                                callbacks.onLogin(false, 104);
                                return;
                            }
                        }
                        callbacks.onSignUp(false, 102);
                    }
                });
    }

    private static void sendVerificationEmail(final FirebaseUser user, UserToken token, final GeneralCallbacks callbacks) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("Send verfication mail", "Completed");
                    uploadUser(token, callbacks);
                    return;
                }
                callbacks.onSignUp(false, 106);
            }
        });
    }

    private static void uploadUser(UserToken token, final GeneralCallbacks callbacks) {
        String email = token.getEmail();
        Bitmap dpMap = token.getDpBitMap();
        StorageReference imageReference = user.getStorageReference().child("Users");
        //convert to byte arraystream and set the compression type
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dpMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();
        final String uniqueKey = email.replace(".", "");
        String Name = email.replace("@gmail.com", "");
        userDetails details = new userDetails(email, Name);
        UploadTask uploadTask = imageReference.child(uniqueKey + "/info/dp.jpg").putBytes(imageData);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.i("Upload User", "Called");
                if (task.isSuccessful()) {
                    uploadUserRealTimeDb(email, details);
                }
            }

            public void uploadUserRealTimeDb(final String email, userDetails userDetails) {
                final String uniqueKey = email.replace(".", "");
                user.getReference().child("Users/" + uniqueKey + "/info/").setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Upload At Real time", " Completed");
                            callbacks.onSignUp(true, -1);
                            return;
                        }
                        callbacks.onSignUp(true, 103);
                    }
                });
            }
        });

    }

    public static void loginUser(final UserToken token, GeneralCallbacks callbacks) {
        user.getAuth().signInWithEmailAndPassword(token.getEmail(), token.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("login User", "successfull ");
                    FirebaseUser firebaseUser = user.getAuth().getCurrentUser();
                    if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                        callbacks.onLogin(true, -1);
                        return;
                    }
                    if (firebaseUser != null && !firebaseUser.isEmailVerified()) {
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete())
                                    callbacks.onLogin(false, 104);
                                else
                                    callbacks.onLogin(false, 106);
                            }
                        });
                    }
                }
                callbacks.onLogin(false, 102);
            }
        });
    }

    //for updating user info
    public static void OnChanged(final String newAccountName, final String newEmail, final String phone, final String country, final String dob, Uri imageuri, GeneralCallbacks callbacks) {
        User users = User.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uniquekey = auth.getCurrentUser().getEmail().toString().replace(".", "");
        if (newAccountName.isEmpty() || newEmail.isEmpty()) {
//            Toast.makeText(getContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            userDetails newDetails = new userDetails(newEmail, newAccountName, phone, country, dob);
            users.getReference().child("Users/" + uniquekey + "/info/").setValue(newDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.e("Upload At Real time", " Completed");
                        callbacks.onUpdate(new userDetails(newEmail, newAccountName, phone, country, dob));
                        return;
                    }
                }
            });

        }
        if (imageuri != null) {
            uploadProfilePicture(imageuri);
        }

    }

    private static void uploadProfilePicture(Uri imageUri) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        User users = User.getInstance();

        if (imageUri != null) {
            String uniqueKey = auth.getCurrentUser().getEmail().replace(".", "");
            StorageReference profilePicRef = users.getStorageReference().child("Users/" + uniqueKey + "/info/dp.jpg");

            profilePicRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle successful upload here (e.g., update the user's profile)
                            Log.d("Upload Profile Picture", "Success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle upload failure
                            Log.e("Upload Profile Picture", "Failed: " + e.getMessage());
                        }
                    });
        }
    }

}
