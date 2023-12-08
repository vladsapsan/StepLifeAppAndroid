package com.StepLife.steplifeapp.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class SectionArticleViewAdapter extends RecyclerView.Adapter<SectionArticleViewAdapter.ViewHolderSection> {

    private ArrayList<LightArticle> mListArticle;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public class ViewHolderSection extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView imageView;

        ViewHolderSection(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.TextArticle);
            imageView = itemView.findViewById(R.id.ImageArticle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public SectionArticleViewAdapter(Context context,ArrayList<LightArticle> mListArticle){
        this.mInflater = LayoutInflater.from(context);
        this.mListArticle = mListArticle;
    }



    @NonNull
    @Override
    public ViewHolderSection onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listviewhomearticle, parent, false);
        return new ViewHolderSection(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSection holder, int position) {
        holder.myTextView.setText(Html.fromHtml(mListArticle.get(position).HeadText).toString());
        //Загрузка картинок с помощью библиотеки
        if(mListArticle.get(position).PreviewPhotoUri!=null) {
                 Glide.with(this.mInflater.getContext()).load(mListArticle.get(position).PreviewPhotoUri).addListener(new RequestListener<Drawable>() {
                     @Override
                     public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                         return false;
                     }

                     @Override
                     public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                         return false;
                     }
                 }).into(holder.imageView);
              //  Picasso.get().load(mListArticle.get(position).PreviewPhotoUri).into(holder.imageView);
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return mListArticle.size();
    }
}
