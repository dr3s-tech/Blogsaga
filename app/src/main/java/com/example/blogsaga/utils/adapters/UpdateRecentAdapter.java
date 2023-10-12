package com.example.blogsaga.utils.adapters;

import android.icu.text.CaseMap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.models.Articles;
import com.example.blogsaga.utils.models.UserTokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpdateRecentAdapter extends RecyclerView.Adapter<UpdateRecentAdapter.RecentHolder> {

    private ArrayList<Articles> RecentSet;
    private int limit;

    public UpdateRecentAdapter(int limit) {
        this.RecentSet=new ArrayList<>();
        this.limit=limit;
    }

    @NonNull
    @Override
    public UpdateRecentAdapter.RecentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_view,parent,false);
        return new RecentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateRecentAdapter.RecentHolder holder, int position) {
        holder.bind(RecentSet.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(RecentSet.size(),limit);
    }

    public void setData(Articles recentArticle) {
        Articles recentArticle1=new Articles(recentArticle.getImageUri(),recentArticle.getTitle(), recentArticle.getDescription());
        RecentSet.add(recentArticle1);
    }

    public class RecentHolder extends RecyclerView.ViewHolder {

        private TextView Tiltle;
        private ShapeableImageView article_imgRecent;
        private ImageView bookmark;
        public RecentHolder(@NonNull View itemView) {
            super(itemView);
            Tiltle=itemView.findViewById(R.id.article_title);
            article_imgRecent=itemView.findViewById(R.id.article_image);
            bookmark=itemView.findViewById(R.id.bookmark_recent);
        }

        public void bind(Articles articles) {
            Tiltle.setText(articles.getTitle());
            String path="Articles/images/"+articles.getImageUri()+"/my-image.jpg";
            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(path);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).placeholder(R.drawable.back_app).error(R.drawable.back_app).into(article_imgRecent);
                }
            });
                ////////////////this bookmark is not wortking checking needed
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserTokens tokens=UserTokens.getInstance();
                    FirebaseAuth auth=tokens.getAuth();
                    String email= auth.getCurrentUser().getEmail().replace(".","");
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users/"+email);
                    String uniqueKey=reference.child("bookmarks").push().getKey();
                    reference.child("bookmarks"+uniqueKey).setValue(uniqueKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            bookmark.setImageResource(R.drawable.baseline_bookmark_24);
                        }
                    });

                }
            });
        }
    }
}
