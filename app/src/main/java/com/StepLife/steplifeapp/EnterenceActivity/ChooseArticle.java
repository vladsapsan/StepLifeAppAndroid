package com.StepLife.steplifeapp.EnterenceActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.garbage.TagSearchArticle;
import com.StepLife.steplifeapp.Adapters.MyRecyclerViewTagsAdapter;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.Model.Article;
import com.StepLife.steplifeapp.Adapters.ArticleListAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ChooseArticle extends AppCompatActivity implements MyRecyclerViewTagsAdapter.ItemClickListener {
    ImageView CloseArticleButton;
    ScrollView DownloadArticleScrollView;
    ArticleListAdapter ArticleListAdapter;

    ListView RecomendationListviewArticle;
    RecyclerView recyclerView;
    StorageReference storageRef ;
    MyRecyclerViewTagsAdapter adapterArticleTags;
    ArrayList<String> mNewArticleTags;
    ProgressBar progressBar;
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    TextView DownloadHeadText,TextDateDownloadArticle,MainTextDownloadArticle,NextArticlesText;


    //считывание нажатия по тегу
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ChooseArticle.this, TagSearchArticle.class);
        intent.putExtra("TagFilter",mNewArticleTags.get(position));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_article);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //Основные элементы статьи
        DownloadHeadText=findViewById(R.id.DownloadHeadText);
        TextDateDownloadArticle=findViewById(R.id.TextDateDownloadArticle);
        MainTextDownloadArticle=findViewById(R.id.MainTextDownloadArticle);
        progressBar=findViewById(R.id.progressBarArticle);

        //Отображение тегов в списке новой статьи
        recyclerView = findViewById(R.id.RecycleviewTagsArticle);
        LinearLayoutManager layoutManager= new LinearLayoutManager(ChooseArticle.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mNewArticleTags = new ArrayList<>();
        adapterArticleTags = new MyRecyclerViewTagsAdapter(ChooseArticle.this,mNewArticleTags);
        adapterArticleTags.setClickListener(ChooseArticle.this);
        recyclerView.setAdapter(adapterArticleTags);



        //Лист рекомендаций в конце статьи
        NextArticlesText  = findViewById(R.id.NextArticlesText);
        NextArticlesText.setVisibility(View.GONE);
        RecomendationListviewArticle = findViewById(R.id.RecomendationListviewArticle);
        ArticleListAdapter = new ArticleListAdapter(getApplicationContext(),R.layout.listviewarticleitem, listTemp);
        RecomendationListviewArticle.setAdapter(ArticleListAdapter);
        RecomendationListviewArticle.setVisibility(View.GONE);

        DownloadArticleScrollView = findViewById(R.id.DownloadArticleScrollView);

        //Закрытие окна
        CloseArticleButton = findViewById(R.id.CloseArticleButton);
        CloseArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();

        //Получение значений через ключ
        Bundle arguments = getIntent().getExtras();
        DownloadHeadText.setText(Html.fromHtml((String) arguments.get("HeaderText"),new GlideImageGetter(DownloadHeadText),null));
        TextDateDownloadArticle.setText((CharSequence) arguments.get("Date"));
        MainTextDownloadArticle.setText(Html.fromHtml((String) arguments.get("MainText"),new GlideImageGetter(MainTextDownloadArticle),null));
        if(arguments.get("TagList")!=null){
            if(mNewArticleTags.size()==0) {
                mNewArticleTags.addAll((ArrayList<String>) arguments.get("TagList"));
                adapterArticleTags.notifyDataSetChanged();
            }
        }
    }



    @Override
    public void finish() {
        super.finish();

    }

    public static void StartArticle(Article article, Activity thisActivity){
        if(article!=null) {
            Intent intentChooseArticle = new Intent(thisActivity, ChooseArticle.class);
            // передача объекта с ключом "MainText" и значением
            intentChooseArticle.putExtra("MainText", article.MainText);
            intentChooseArticle.putExtra("Date", article.Date);
            intentChooseArticle.putExtra("HeaderText", Html.fromHtml(article.HeadText).toString().trim());
            if(article.TagList!=null){
                intentChooseArticle.putStringArrayListExtra("TagList", article.TagList);
            }
            // запуск ChooseArticle
            thisActivity.startActivity(intentChooseArticle);
        }
    }
}