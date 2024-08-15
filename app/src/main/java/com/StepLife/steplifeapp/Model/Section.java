package com.StepLife.steplifeapp.Model;

import java.util.ArrayList;
import java.util.List;


//Класс раздела
public class Section {
    public String SectionName;
    public String AboutSection;
    public String SectionID;
    public ArrayList<LightArticle> articleList;
    public Section()
    {}

    public Section(String Name, String About,String SectionID, ArrayList<LightArticle> articleList){
        this.SectionName = Name;
        this.AboutSection = About;
        this.SectionID = SectionID;
        this.articleList = articleList;
    }
}
