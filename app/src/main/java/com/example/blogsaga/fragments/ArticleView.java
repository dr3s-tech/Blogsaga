package com.example.blogsaga.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.models.Articles;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ArticleView extends Fragment {

    ImageView cross,ArtViewImage;
    TextView TitelView,descriptionView;

    Articles articles;

    public ArticleView(Articles articles) {
        this.articles = articles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_article_view, container, false);
        init(root);
        TitelView.setText(articles.getTitle());
        descriptionView.setText(articles.getDescription());
        String path="Articles/images/"+articles.getImageUri()+"/my-image.jpg";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(path);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.back_app).error(R.drawable.back_app).into(ArtViewImage);
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new HomePage());
            }
        });
        return root;
    }

    private void init(View root) {
        cross=root.findViewById(R.id.cross_AtView);
        ArtViewImage=root.findViewById(R.id.article_image_view);
        TitelView=root.findViewById(R.id.TitelView);
        descriptionView=root.findViewById(R.id.descriptionView);
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
}