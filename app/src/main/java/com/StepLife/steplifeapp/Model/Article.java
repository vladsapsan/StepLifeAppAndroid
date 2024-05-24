package com.StepLife.steplifeapp.Model;

import java.util.ArrayList;

public class Article {

    public String id,Date,HeadText;
    public ArrayList<String> TagList;
    public ArrayList<String> RecomendationList;
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

    public Article(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri,ArrayList<String> TagList){
        this.id = id;
        this.Date = Date;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
        this.MainText = MainText;
        this.TagList = TagList;
    }

    public Article(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri,ArrayList<String> TagList,ArrayList<String> RecList){
        this.id = id;
        this.Date = Date;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
        this.MainText = MainText;
        this.TagList = TagList;
        this.RecomendationList = RecList;
    }



}
