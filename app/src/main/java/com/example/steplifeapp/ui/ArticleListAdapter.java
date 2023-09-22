package com.example.steplifeapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.steplifeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleListAdapter extends ArrayAdapter <Article> {
    private Context mContext;
    private int mResource;
    private ArrayList<Article> mDisplayedValues;
    private List<Article> mOriginalValues;


    public ArticleListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Article> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        mDisplayedValues = objects;
        mOriginalValues = objects;
    }

    @SuppressLint("SuspiciousIndentation")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Article article = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent,false);
        TextView HeadText = convertView.findViewById(R.id.ArticleHeadTextItems);
        TextView Date = convertView.findViewById(R.id.ArticleDateTextItems);
        ImageView PreviewImage = convertView.findViewById(R.id.PreviewPhoto);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBarDownloadpreviewPhotoArticle);

        //Загрузка картинок с помощью библиотеки пикассо
        if(article.PreviewPhotoUri!=null) {
            if(PreviewImage.getDrawable()==null) {
                // Glide.with(mContext).load(article.PreviewPhotoUri).into(PreviewImage);
                Picasso.get().load(article.PreviewPhotoUri).into(PreviewImage);
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


        HeadText.setText(Html.fromHtml(article.HeadText).toString().trim());
        Date.setText(article.Date);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = mOriginalValues.size();
                    filterResults.values = mOriginalValues;

                }else{
                    List<Article> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Article article:mOriginalValues){
                        String HeadText = Html.fromHtml(article.HeadText).toString();
                        String Date = Html.fromHtml(article.Date).toString();
                        if(HeadText.contains(searchStr) || Date.contains(searchStr)){
                            resultsModel.add(article);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<Article>) results.values;
                notifyDataSetChanged();
                Log.d("Text", String.valueOf(results.count));
            }
        };
        return filter;
    }








}
