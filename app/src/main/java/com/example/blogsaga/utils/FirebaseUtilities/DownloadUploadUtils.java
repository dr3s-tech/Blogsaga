package com.example.blogsaga.utils.FirebaseUtilities;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.blogsaga.utils.models.Articles;
import com.example.blogsaga.utils.models.BaseArticle;
import com.example.blogsaga.utils.models.UserTokens;
import com.example.blogsaga.utils.models.userDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DownloadUploadUtils {
    private static UserTokens tokens = UserTokens.getInstance();
    private static FirebaseAuth auth = tokens.getAuth();
    private static DatabaseReference database = tokens.getDatabaseReference();

    private static String generateUniqueKey() {
        long timestamp = System.currentTimeMillis();
        int randomValue = (int) (Math.random() * 100000); // Adjust the range as needed
        return timestamp + "_" + randomValue;
    }

    public static void uploadArticle(Articles articles) {
        Log.i("upload articles called", "In upload article");
        final String email = auth.getCurrentUser().getEmail().replace(".", "");
        UserTokens token = UserTokens.getInstance();
        String uniqueRef = generateUniqueKey();

        System.out.println(uniqueRef + "What the fuck");
        StorageReference storageRef = token.getFirebaseStorage().getReference("Users/" + email + "/articles/images/" + uniqueRef);
        StorageReference artReference=token.getFirebaseStorage().getReference("Articles/images/"+uniqueRef).child("/my-image.jpg");
        artReference.putBytes(articles.getImageBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Article created in storage");
            }
        });
        StorageReference imageRef = storageRef.child("my-image" + ".jpg");
        imageRef.putBytes(articles.getImageBytes())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // The image has been uploaded successfully

                        articles.setImageUri(uniqueRef);
                        String uniqueKey = database.child("Articles").push().getKey();
                        System.out.println(uniqueKey+" downloadUtils 59");
                        BaseArticle article = new BaseArticle(articles,uniqueKey);
                        System.out.println(article.getId()+" hello 61");
                        database.child("Articles/" + uniqueKey).setValue(article)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            database.child("Users/" + email + "/articles").push().setValue(uniqueKey);
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // The image upload failed
                        Log.e("TAG", "Image upload failed", e);
                    }
                });

    }

    public static void updationFollowerFollowing(final String email){
       final String ownerEmail=auth.getCurrentUser().getEmail().replace(".","");
                    database.child("Users/"+ownerEmail+"/info").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer value=snapshot.child("following").getValue(Integer.class);
                            value++;
                            database.child("Users/"+ownerEmail+"/info/following").setValue(value);
                            database.child("Users/"+email+"/info").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Integer followers=snapshot.child("followers").getValue(Integer.class);
                                    followers++;
                                    database.child("Users/"+email+"/info/followers").setValue(followers);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        database.child("Users/"+ownerEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database.child("Users/"+ownerEmail+"/following").push().setValue(email);
                database.child("Users/"+email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        database.child("Users/"+email+"/followers").push().setValue(ownerEmail);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
