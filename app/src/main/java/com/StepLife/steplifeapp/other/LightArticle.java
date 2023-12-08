package com.StepLife.steplifeapp.other;

import java.util.ArrayList;

public class LightArticle {
    public String id,HeadText;
    public ArrayList<String> TagList;
    public String PreviewPhotoUri;
    public LightArticle()
    {}
    public LightArticle(String id, String Date, String HeadText,String MainText){
        this.id = id;
        this.HeadText = HeadText;
    }

    public LightArticle(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri){
        this.id = id;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
    }

    public LightArticle(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri,ArrayList<String> TagList){
        this.id = id;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
        this.TagList = TagList;
    }

    public LightArticle(String id, String Date, String HeadText,String MainText,String PreviewPhotoUri,ArrayList<String> TagList,ArrayList<String> RecList){
        this.id = id;
        this.HeadText = HeadText;
        this.PreviewPhotoUri = PreviewPhotoUri;
        this.TagList = TagList;
    }
}
