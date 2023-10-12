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
import android.widget.Toast;
import com.example.blogsaga.R;
import com.example.blogsaga.utils.adapters.UpdateArticlesAdapter;
import com.example.blogsaga.utils.adapters.UpdateRecentAdapter;
import com.example.blogsaga.utils.callbacks.RecyclerCallbacks;
import com.example.blogsaga.utils.models.Articles;
import com.example.blogsaga.utils.models.UserTokens;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HomePage extends Fragment implements RecyclerCallbacks {

    FloatingActionButton createbtn,searchBtn,profileBtn;
    ImageView notificationbtn, bookmarkbtn;
    RecyclerView ownRv,recentRv;
    UpdateArticlesAdapter ownAdapter;
    UpdateRecentAdapter recentAdapter;
    LinearLayoutManager LayoutManager,recentLayoutManager;
    ArrayList<Articles> dataset,recentset;
    ChildEventListener ownArticleListener,recentArticlesListner;
    DatabaseReference ownArticleReference,recentArticleReference;
    FirebaseAuth auth;
    UserTokens token;
//    UpdateArticlesAdapter Adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_page, container, false);
        initRecyclerview(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new CreatePage());
            }
        });

//        notificationbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addFragment(new Notification());
//            }
//        });
        bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new MyBookmark());
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new Search());
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new Profile());
            }
        });

    }

    private void initRecyclerview(View view) {
        dataset = new ArrayList<>();
        recentset=new ArrayList<>();
        createbtn = view.findViewById(R.id.create_button);
        searchBtn=view.findViewById(R.id.search_btn);
        profileBtn=view.findViewById(R.id.profile_btn);
        ownRv = view.findViewById(R.id.urartclerecyclerview);
        recentRv=view.findViewById(R.id.recent_Recyclerview);
//        notificationbtn = view.findViewById(R.id.notification);
        bookmarkbtn = view.findViewById(R.id.bookmark);
        ownAdapter = new UpdateArticlesAdapter(this);
        recentAdapter=new UpdateRecentAdapter(20,this);
        token = UserTokens.getInstance();
        LayoutManager = new LinearLayoutManager(getContext());
        recentLayoutManager=new LinearLayoutManager(getContext());
        LayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recentLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        System.out.println("Adapter attached here");
        ownRv.setAdapter(ownAdapter);
        recentRv.setAdapter(recentAdapter);   //this is add by me
        ownRv.setLayoutManager(LayoutManager);
        recentRv.setLayoutManager(recentLayoutManager);  //this is add by me
        auth = token.getAuth();
        ownArticleReference = token.getDatabaseReference().child("Users/" + auth.getCurrentUser().getEmail().replace(".", "") + "/articles");
        recentArticleReference= token.getDatabaseReference().child("Articles");
        ownArticleListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String articleNumber = snapshot.getValue(String.class);
                System.out.println(articleNumber);
                DatabaseReference reference = token.getDatabaseReference().child("Articles/" + articleNumber);
                reference.
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                System.out.println("This guy got called 120");
                                Articles article = snapshot.getValue(Articles.class);
                                System.out.println("This guy got called 137");

                                if(article!=null) {
                                    ownAdapter.setData(article);
                                    System.out.println(article.getImageUri()+" 125");
                                    ownAdapter.notifyDataSetChanged();
                                }
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
        ownArticleReference.addChildEventListener(ownArticleListener);
        //this is add by me not working properly giving error
        recentArticlesListner=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String recentnumber= snapshot.getKey();
                recentArticleReference.child(recentnumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Articles recentArticles=snapshot.getValue(Articles.class);
                        if(recentArticles!=null){
                            recentAdapter.setData(recentArticles);
                            recentAdapter.notifyDataSetChanged();
                        }
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

        recentArticleReference.addChildEventListener(recentArticlesListner);

    }



    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(Articles articles) {
        Toast.makeText(getContext(), "Article is clicked", Toast.LENGTH_SHORT).show();
        addFragment(new ArticleView(articles));
        
    }
}