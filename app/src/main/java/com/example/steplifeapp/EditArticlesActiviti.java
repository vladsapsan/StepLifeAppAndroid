package com.example.steplifeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.steplifeapp.ui.Article;
import com.example.steplifeapp.ui.ArticleListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class EditArticlesActiviti extends AppCompatActivity {

    ImageView imagebackEditArticles;
    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private ArrayAdapter<String> adapter;


    private List<String> listData;
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    ProgressBar progressBar;
    Article DowArticle;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;

    private Target mTarget;
    private String Article_Key ="AllArticle";
    private DatabaseReference mDataBase;

    //Иницилизация компонентов
    private void initilization()
    {
        allArticlelist = findViewById(R.id.AllArticleListview);
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(this,R.layout.listvieweditarticleitem, listTemp);
        allArticlelist.setAdapter(ArticleListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_articles_activiti);


        //Дефолт стиль
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

       // initilization();



        imagebackEditArticles = findViewById(R.id.imagebackEditArticles);
        imagebackEditArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}