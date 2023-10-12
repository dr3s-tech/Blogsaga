package com.example.blogsaga.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.adapters.UpdateBookmarkAdapter;
import com.example.blogsaga.utils.models.Articles;
import com.example.blogsaga.utils.models.UserTokens;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

                        ////////////////Not checked//////////
public class MyBookmark extends Fragment {

    ImageView backBookmark;
    RecyclerView bookMarkView;
    LinearLayoutManager layoutManager;
    UpdateBookmarkAdapter bookmarkAdapter;

    ArrayList<Articles> dataset;
    ChildEventListener bookmarkListner;
    DatabaseReference bookMarkReference;
    DatabaseReference articleReference;
    UserTokens token;
    FirebaseAuth auth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_my_bookmark, container, false);
        init(root);

        backBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new HomePage());
            }
        });
        
        return root;
    }

    private void init(View root) {
        backBookmark=root.findViewById(R.id.back_button);
        bookMarkView=root.findViewById(R.id.bookmark_view);
        dataset=new ArrayList<>();
        token = UserTokens.getInstance();
        bookmarkAdapter=new UpdateBookmarkAdapter(dataset);
        layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        bookMarkView.setLayoutManager(layoutManager);
        bookMarkView.setAdapter(bookmarkAdapter);
        auth = token.getAuth();
        articleReference=token.getDatabaseReference().child("Articles/");
        bookMarkReference= token.getDatabaseReference().child("Users/"+auth.getCurrentUser().getEmail()
                .replace(".","")+"/bookmarks");
        bookmarkListner=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String articleKey=snapshot.getValue(String.class);
                articleReference.child(articleKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final Articles articles=snapshot.getValue(Articles.class);
                        System.out.println(articles.getTitle());
                        System.out.println(articles.getDescription());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        bookMarkReference.addChildEventListener(bookmarkListner);

    }
    public void addFragment(Fragment fragment) {
    FragmentManager fragmentManager = getFragmentManager();
    assert fragmentManager != null;
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.container, fragment);
    fragmentTransaction.commit();
                            }
}