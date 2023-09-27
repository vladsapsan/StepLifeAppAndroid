package com.StepLife.steplifeapp.ui;

import java.util.List;

public class Article {

    public String id,Date,HeadText;
    public List<String> TagList;
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

    public Article(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri,List<String> TagList){
        this.id = id;
        this.Date = Date;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
        this.MainText = MainText;
        this.TagList = TagList;
    }



}
