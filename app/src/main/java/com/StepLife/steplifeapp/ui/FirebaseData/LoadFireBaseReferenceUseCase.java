package com.StepLife.steplifeapp.ui.FirebaseData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoadFireBaseReferenceUseCase {
    public static final String Library_Key ="Lib";
    public static final String Article_Key ="AllArticle";
    public static final String Section_Article_Key ="AllArticleSection";
    public static DatabaseReference getDataBaseReference(String Key){
        return FirebaseDatabase.getInstance().getReference().child(Key);
    }
}
