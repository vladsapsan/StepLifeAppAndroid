package com.StepLife.steplifeapp.FirebaseData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseDataManager {
    public static final String Library_Key ="Lib";

    public static final String Article_Key ="AllArticle";
    public static final String Section_Article_Key ="AllArticleSection";
    public static DatabaseReference getDataBaseReference(String Key){
        return FirebaseDatabase.getInstance().getReference().child(Key);
    }
    public static void getDataFromFirebase(String dataPath, ValueEventListener listener) {
        FirebaseDatabase.getInstance().getReference(dataPath).addListenerForSingleValueEvent(listener);
    }


}
