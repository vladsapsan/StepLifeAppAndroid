package com.example.steplifeapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.steplifeapp.R;
import com.example.steplifeapp.ui.Article;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ArticleListAdapter extends ArrayAdapter <Article> {
    private Context mContext;
    private int mResource;


    public ArticleListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Article> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @SuppressLint("SuspiciousIndentation")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent,false);
        TextView HeadText = convertView.findViewById(R.id.ArticleHeadTextItems);
        TextView Date = convertView.findViewById(R.id.ArticleDateTextItems);
        ImageView PreviewImage = convertView.findViewById(R.id.PreviewPhoto);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBarDownloadpreviewPhotoArticle);


        //Загрузка картинок с помощью библиотеки пикассо
        if(getItem(position).PreviewPhotoUri!=null) {
            if(PreviewImage.getDrawable()==null) {
                Picasso.get().load(getItem(position).PreviewPhotoUri).into(PreviewImage);
            }
        }


        //Загрузка картинки с помощью Glide
     //   if(getItem(position).PreviewPhotoUri!=null) {
      //      if(PreviewImage.getDrawable()==null) {
      //          Glide
      //                  .with(this.getContext())
      //                  .load(getItem(position).PreviewPhotoUri)
       //                 .centerCrop()
       //                 .diskCacheStrategy(DiskCacheStrategy.ALL)
       //                 .into(PreviewImage);
      //      }
     //   }


        HeadText.setText(Html.fromHtml(getItem(position).HeadText).toString().trim());

        //HeadText.setText(Html.fromHtml(getItem(position).HeadText));
        Date.setText(getItem(position).Date);
        return convertView;
    }









}
