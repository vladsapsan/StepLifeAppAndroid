package com.StepLife.steplifeapp.other;

import java.util.List;


//Класс раздела
public class Section {
    public String SectionName;
    public String AboutSection;
    public String SectionID;
    public List<LightArticle> articleList;

    public Section()
    {}

    public Section(String Name, String About,String SectionID, List<LightArticle> articleList){
        this.SectionName = Name;
        this.AboutSection = About;
        this.SectionID = SectionID;
        this.articleList = articleList;
    }
}
