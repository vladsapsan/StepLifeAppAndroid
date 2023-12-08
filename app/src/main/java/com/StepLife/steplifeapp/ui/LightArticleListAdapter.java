package com.StepLife.steplifeapp.ui;


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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.LightArticle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LightArticleListAdapter extends ArrayAdapter <LightArticle>  {
    private Context mContext;
    private int mResource;
    private ArrayList<LightArticle> mDisplayedValues;
    private ArrayList<LightArticle> mOriginalValues;


    public LightArticleListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LightArticle> objects) {
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

        LightArticle article = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent,false);
        TextView HeadText = convertView.findViewById(R.id.ArticleHeadTextItems);
        ImageView PreviewImage = convertView.findViewById(R.id.PreviewPhoto);
        TextView TextviewTag  = convertView.findViewById(R.id.TextviewTag);
        CardView MoreTagsArticle = convertView.findViewById(R.id.MoreTagsArticle);
        TextView TextviewMoreTag = convertView.findViewById(R.id.TextviewMoreTag);
        CardView AddTagsArticleCard = convertView.findViewById(R.id.AddTagsArticleCard);
        //Загрузка картинок с помощью библиотеки
        if(article.PreviewPhotoUri!=null) {
            if(PreviewImage.getDrawable()==null) {
                // Glide.with(mContext).load(article.PreviewPhotoUri).into(PreviewImage);
                // Picasso.get().load(article.PreviewPhotoUri).into(PreviewImage);
            }
        }

        if(article.TagList!=null){
            AddTagsArticleCard.setVisibility(View.VISIBLE);
            TextviewTag.setText(article.TagList.get(0));
            if(article.TagList.size()>1){
                MoreTagsArticle.setVisibility(View.VISIBLE);
                TextviewMoreTag.setText("+"+(article.TagList.size()-1));
            }
        }
        //Загрузка картинки  Glide
        if(getItem(position).PreviewPhotoUri!=null) {
            if(PreviewImage.getDrawable()==null) {
                Picasso.get().load(article.PreviewPhotoUri).into(PreviewImage);
            }
        }
        HeadText.setText(Html.fromHtml(article.HeadText).toString().trim());
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
                    List<LightArticle> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();
                    for(LightArticle article:mOriginalValues){
                        String HeadText = Html.fromHtml(article.HeadText).toString();
                        if(HeadText.contains(searchStr)){
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
                mDisplayedValues = (ArrayList<LightArticle>) results.values;
                notifyDataSetChanged();
                Log.d("Text", String.valueOf(results.count));
            }
        };
        return filter;
    }




}

