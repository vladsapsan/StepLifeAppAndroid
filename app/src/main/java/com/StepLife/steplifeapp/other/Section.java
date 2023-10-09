package com.StepLife.steplifeapp.other;

import com.StepLife.steplifeapp.ui.Article;

import java.util.List;


//Класс раздела
public class Section {
    String SectionName;
    String AboutSection;
    List<Article> articleList;

    Section(String Name,String About,List<Article> articleList){
        this.SectionName = Name;
        this.AboutSection = About;
        this.articleList = articleList;
    }
}
