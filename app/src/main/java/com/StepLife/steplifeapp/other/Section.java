package com.StepLife.steplifeapp.other;

import com.StepLife.steplifeapp.ui.Article;

import java.util.List;


//Класс раздела
public class Section {
    public String SectionName;
    public String AboutSection;
    public String SectionID;
    public List<Article> articleList;

    public Section()
    {}

    public Section(String Name, String About,String SectionID, List<Article> articleList){
        this.SectionName = Name;
        this.AboutSection = About;
        this.SectionID = SectionID;
        this.articleList = articleList;
    }
}
