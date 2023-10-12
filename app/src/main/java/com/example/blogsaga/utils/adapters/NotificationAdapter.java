package com.example.blogsaga.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.callbacks.NotificationCallbacks;
import com.example.blogsaga.utils.models.NotificationModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private ArrayList<NotificationModel> notificationSet;
    Context context;
NotificationCallbacks listner;
    public NotificationAdapter(ArrayList<NotificationModel> notificationSet ,Context context, NotificationCallbacks listner){
        this.notificationSet=notificationSet;
        this.listner=listner;
        this.context=context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notificationSet.size();
    }

    public void setNotificationSet(ArrayList<NotificationModel> notificationSet){
        this.notificationSet=notificationSet;
        this.notifyDataSetChanged();
    }
    public class NotificationHolder extends RecyclerView.ViewHolder {
        NotificationCallbacks callbacks;
        ImageView postimage;
        ShapeableImageView profileImage;
        TextView username,text;

        public NotificationHolder(@NonNull View view) {
            super(view);
            profileImage=view.findViewById(R.id.AC_image);
            postimage=view.findViewById(R.id.postImage);
            username=view.findViewById(R.id.UserName);
            text=view.findViewById(R.id.messege);
        }
    }

}
