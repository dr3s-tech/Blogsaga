package com.example.blogsaga.utils.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogsaga.R;
import com.example.blogsaga.utils.models.Articles;

import java.util.ArrayList;

public class UpdateBookmarkAdapter extends RecyclerView.Adapter<UpdateBookmarkAdapter.BookmarkHolder> {

    private ArrayList<Articles> BookmarSet;

    public UpdateBookmarkAdapter(ArrayList<Articles> bookmarSet) {
        BookmarSet = bookmarSet;
    }

    @NonNull
    @Override
    public UpdateBookmarkAdapter.BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_drafts_rv_item,parent,false);
        return new BookmarkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateBookmarkAdapter.BookmarkHolder holder, int position) {
        holder.bind(BookmarSet.get(position));
    }

    public void setData(Articles BookmarkArticle){BookmarSet.add(BookmarkArticle);}

    @Override
    public int getItemCount() {
        return BookmarSet.size();
    }

    public class BookmarkHolder extends RecyclerView.ViewHolder {

        private TextView Title;

        public BookmarkHolder(@NonNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.article_title);
        }
        public void bind(Articles articles){
            System.out.println("bookmarkAdaapter");
            Title.setText(articles.getTitle());
        }
    }
}
