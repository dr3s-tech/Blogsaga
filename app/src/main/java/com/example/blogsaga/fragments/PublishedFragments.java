package com.example.blogsaga.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogsaga.R;
//import com.example.blogsaga.utils.adapters.UpdateDraftAdapter;
//import com.example.blogsaga.utils.adapters.UpdatePublishedAdapter;
import com.example.blogsaga.utils.callbacks.RecyclerBookmarkCallbacks;
import com.example.blogsaga.utils.models.Articles;

import java.util.List;

public class PublishedFragments extends Fragment implements RecyclerBookmarkCallbacks {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Articles> dataset;
//    UpdatePublishedAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_published, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
//        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        layoutManager=new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        adapter=new UpdatePublishedAdapter(dataset, getContext(), this);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

//    private void initData() {
//        dataset=new ArrayList<>();
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//        dataset.add(new Articles(R.drawable.recentpic,R.drawable.recentpic,"Nasty fitness","8 days ago","5 tips for stay fit and healthy"));
//
//    }

    @Override
    public void onClick(Articles articles) {

    }
}