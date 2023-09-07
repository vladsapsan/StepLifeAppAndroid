package com.example.steplifeapp.ui;

import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

public class Article {

    public String id,Date,HeadText;
    public String MainText;
    public String PreviewPhotoUri;
    public Article()
    {}
    public Article(String id, String Date, String HeadText,String MainText){
        this.id = id;
        this.Date = Date;
        this.HeadText = HeadText;
        this.MainText = MainText;
    }

    public Article(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri){
        this.id = id;
        this.Date = Date;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
        this.MainText = MainText;
    }



}
