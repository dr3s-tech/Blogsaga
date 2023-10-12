package com.example.blogsaga.fragments;

import static android.app.Activity.RESULT_OK;

import static com.example.blogsaga.utils.FirebaseUtilities.GeneralFirebaseUtiliities.OnChanged;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.blogsaga.R;
import com.example.blogsaga.utils.callbacks.GeneralCallbacks;
import com.example.blogsaga.utils.models.User;
import com.example.blogsaga.utils.models.userDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class EditProfile extends Fragment {
    private ImageView backbutton;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    GeneralCallbacks callbacks;
    FloatingActionButton changeProfilePic;
    ShapeableImageView profile_pic;
    EditText nameEt,phoneEt,countryEt,dobEt;
    TextView emailEt;
    AppCompatButton save;
    private DatePickerDialog picker;
    private static User users = User.getInstance();
    private  StorageReference imageRef;

    private static final int Gallery_pick=1;
    private Uri imageuri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        init(root);
        //fetching the current user email and make the uniquekey
        String email = auth.getCurrentUser().getEmail().toString();
        final String uniqueKey = email.replace(".", "");
        imageRef=FirebaseStorage.getInstance().getReference().child("Users/"+uniqueKey+"/info/dp.jpg");
        loadProfilePicture(uniqueKey);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new Profile());
            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAccountName=nameEt.getText().toString();
                String newEmail=emailEt.getText().toString();
                String phone=phoneEt.getText().toString();
                String country=countryEt.getText().toString();
                String dob=dobEt.getText().toString();
                OnChanged(newAccountName,newEmail,phone,country,dob,imageuri,callbacks);
//                addFragment(new Profile());
            }
        });
        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                picker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobEt.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },year,month,day);
                picker.show();
            }
        });




        return root;
    }
    private void loadProfilePicture(final String key) {
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Use a library like Picasso or Glide to load the image into the ImageView
                Picasso.get().load(uri).into(profile_pic);
                Log.e("Load Profile picture","done");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors when loading the profile picture
                Log.e("Load Profile Picture", "Failed: " + exception.getMessage());
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef=databaseReference.child("Users/"+key+"/info");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userDetails details=snapshot.getValue(userDetails.class);
                    if(details!=null){
                        nameEt.setText(details.getName());
                        emailEt.setText(details.getEmail());
                        phoneEt.setText(details.getPhone());
                        countryEt.setText(details.getCountry());
                        dobEt.setText(details.getDob());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
            }
        });

    }



    private void init(final View root) {
        backbutton=root.findViewById(R.id.back_button);
        nameEt=root.findViewById(R.id.name);
        emailEt=root.findViewById(R.id.email);
        phoneEt=root.findViewById(R.id.phone);
        countryEt=root.findViewById(R.id.country);
        dobEt=root.findViewById(R.id.DOB);
        save=root.findViewById(R.id.save_btn);
        profile_pic=root.findViewById(R.id.profile_pic);
        changeProfilePic=root.findViewById(R.id.change_profile_button);
        callbacks=new GeneralCallbacks() {
            @Override
            public void onSignUp(boolean flag, int errorCode) {

            }

            @Override
            public void onLogin(boolean flag, int errorCode) {

            }

            @Override
            public void onUpdate(userDetails details) {
                String name=details.getName();
                String phone=details.getPhone();
                String country=details.getCountry();
                String email=details.getEmail();
                String dob=details.getDob();
                nameEt.setText(name);
                emailEt.setText(email);
                phoneEt.setText(phone);
                countryEt.setText(country);
                dobEt.setText(dob);
            }
        };
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    private void OpenGallery(){
        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_pick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_pick && resultCode==RESULT_OK && data!=null){
            imageuri= data.getData();
            profile_pic.setImageURI(imageuri);

        }
    }


}