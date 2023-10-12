package com.example.blogsaga.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.adapters.TabAdapter;
import com.example.blogsaga.utils.callbacks.GeneralCallbacks;
import com.example.blogsaga.utils.models.userDetails;
import com.example.blogsaga.utils.services.UpdateFollowersService;
import com.example.blogsaga.utils.services.UploadArticleService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserDetailsBySearch extends Fragment {

    public TextView followbtn;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    ShapeableImageView profile;
    GeneralCallbacks callbacks;
    ProgressBar progressBar;
    TextView UserName,Useremail;
    ImageView back;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private StorageReference imageRef;
    String uniqueKey;
    TextView followerNo,followingNo;

    public UserDetailsBySearch(String uniqueKey) {
        this.uniqueKey=uniqueKey;
        System.out.println(uniqueKey+" is the email 60");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_user_details_by_search, container, false);
        init(root);

        imageRef= FirebaseStorage.getInstance().getReference().child("Users/"+uniqueKey+"/info/dp.jpg");
        loadProfilePicture(uniqueKey);
        tabLayout.addTab(tabLayout.newTab().setText("Drafts"));
        tabLayout.addTab(tabLayout.newTab().setText("Published"));

        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        TabAdapter adapter = new TabAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upDateFolloDetails=new Intent(getActivity(), UpdateFollowersService.class);
                upDateFolloDetails.putExtra("EMAIL", uniqueKey);
                System.out.println("onclick"+uniqueKey);
                requireActivity().startService(upDateFolloDetails);
                System.out.println("onclick"+uniqueKey);
            }

        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new HomePage());
            }
        });


        return root;
    }


    private void init(final View root) {
        followbtn = root.findViewById(R.id.follow_btn);
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager2 = root.findViewById(R.id.viewPager2);
        profile=root.findViewById(R.id.profile_pic);
        back=root.findViewById(R.id.back_button_Usv);
        UserName=root.findViewById(R.id.User_name);
        Useremail=root.findViewById(R.id.userEmailTv);
        progressBar=root.findViewById(R.id.progressBar_Usv);
        followerNo=root.findViewById(R.id.follower_no);
        followingNo=root.findViewById(R.id.following_no);
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
                UserName.setText(name);
            }
        };
        progressBar.setVisibility(View.VISIBLE);
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    private void loadProfilePicture(final String key) {
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Use a library like Picasso or Glide to load the image into the ImageView
                Picasso.get().load(uri).into(profile);
                Log.e("Load Profile picture","done");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors when loading the profile picture
                Log.e("Load Profile Picture", "Failed: " + exception.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef=databaseReference.child("Users/"+key+"/info");
        ///this is done by Suraj
        DatabaseReference followingref=userRef.child("following");
        //done
        followingref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer followdetails=snapshot.getValue(Integer.class);
                    if (followdetails!=null){
                        String following=Integer.toString(followdetails);
                        followingNo.setText(following);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

        DatabaseReference followref=FirebaseDatabase.getInstance().getReference().child("Users/"+key+"/info");
        followref.child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer followerdetails=snapshot.getValue(Integer.class);
                    if (followerdetails!=null){
                        String follower=Integer.toString(followerdetails);
                        followerNo.setText(follower);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userDetails details=snapshot.getValue(userDetails.class);
                    if(details!=null){
                        String user=details.getName();
                        UserName.setText(user);
                        Useremail.setText(details.getEmail());
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
                    //by Suraj
}