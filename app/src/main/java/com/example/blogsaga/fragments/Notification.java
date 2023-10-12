package com.example.blogsaga.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.adapters.NotificationAdapter;
import com.example.blogsaga.utils.callbacks.NotificationCallbacks;
import com.example.blogsaga.utils.models.Articles;
import com.example.blogsaga.utils.models.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Notification extends Fragment implements NotificationCallbacks {
   ImageView back;
   RecyclerView notificationView;
    LinearLayoutManager LayoutManager;
    ArrayList<NotificationModel> notificationlist;
    NotificationAdapter adapter;
    DatabaseReference reference;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    ChildEventListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_notification, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationView=view.findViewById(R.id.notificationView);
        back=view.findViewById(R.id.back_button);
        String uniquekey=auth.getCurrentUser().getEmail().toString().replace(".","");

        reference= FirebaseDatabase.getInstance().getReference().child("Users/"+uniquekey+"/Articles");
        listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Articles articles=snapshot.getValue(Articles.class);
                NotificationModel model=new NotificationModel();
                notificationlist.add(model);
                adapter.notifyDataSetChanged();
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
        initData();
        initRecyclerview();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new HomePage());
            }
        });
    }

    private void initRecyclerview() {
        LayoutManager=new LinearLayoutManager(requireContext());
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        notificationView.setLayoutManager(LayoutManager);
        adapter=new NotificationAdapter(notificationlist,getContext(),this);
        notificationView.setAdapter(adapter);

    }

    private void initData() {

    }

    @Override
    public void onClick(NotificationModel notification) {

    }
    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
}