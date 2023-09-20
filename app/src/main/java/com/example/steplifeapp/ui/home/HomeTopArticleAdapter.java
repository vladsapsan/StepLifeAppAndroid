package com.example.steplifeapp.ui.home;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steplifeapp.R;
import com.example.steplifeapp.ui.Article;

import java.util.List;


public class HomeTopArticleAdapter  extends  RecyclerView.Adapter<HomeTopAdapter>{

    List<Article> items;

    public HomeTopArticleAdapter(List<Article> items){
        this.items = items;
    }

    @NonNull
    @Override
    public HomeTopAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewhomearticle,parent,false);
        return new HomeTopAdapter(view).linkAdapter(this);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeTopAdapter holder, int position) {
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}



class HomeTopAdapter extends RecyclerView.ViewHolder{
    TextView textView;
    Uri ImageUri;
    ImageView imageView;
    private HomeTopArticleAdapter adapter;

    public HomeTopAdapter(@NonNull View itemView) {
        super(itemView);

    }

    public HomeTopAdapter linkAdapter(HomeTopArticleAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}