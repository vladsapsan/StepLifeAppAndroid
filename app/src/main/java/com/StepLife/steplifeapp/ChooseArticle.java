package com.StepLife.steplifeapp;

import static com.StepLife.steplifeapp.AllArticle.getDeviceWidth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.StepLife.steplifeapp.other.MyRecyclerViewTagsAdapter;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ChooseArticle extends AppCompatActivity implements MyRecyclerViewTagsAdapter.ItemClickListener {
    ImageView CloseArticleButton;
    ScrollView DownloadArticleScrollView;
    ArticleListAdapter ArticleListAdapter;

    ListView RecomendationListviewArticle;
    RecyclerView recyclerView;
    Uri DownloadphotoUri;
    StorageReference storageRef ;
    MyRecyclerViewTagsAdapter adapterArticleTags;
    ArrayList<String> mNewArticleTags;
    ProgressBar progressBar;
    Bitmap bitmap1 = null;
    private ArrayList <Article> listTemp = new ArrayList<Article>();

    Drawable drawable;
    SwipeRefreshLayout SwipeRefreshArticle;
    NetworkChangeListner networkChangeListner;

    private Target mTarget;

    TextView DownloadHeadText,TextDateDownloadArticle,MainTextDownloadArticle,NextArticlesText;


    //считывание нажатия по тегу
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ChooseArticle.this, TagSearchArticle.class);
        intent.putExtra("TagFilter",mNewArticleTags.get(position));
        startActivity(intent);
    }


    private class ImageGetter implements Html.ImageGetter {
        int countimage = 0;
        int loadedcount = 0;
        public Drawable getDrawable(String source) {
            int id;
            DownloadphotoUri = Uri.parse(source);
            bitmap1 = null;

            drawable = null;
            id = R.drawable.buttonimage;

            mTarget = new Target() {

               @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmap1 = bitmap;
                    drawable = new BitmapDrawable(getResources(),bitmap);
                    loadedcount++;
                   MainTextDownloadArticle.setVisibility(View.VISIBLE);
                    Recreatetool();
                   progressBar.setVisibility(View.GONE);
                }

                @Override
               public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            };
            Picasso.get().load(DownloadphotoUri).into(mTarget);

            if(bitmap1 != null)
            {
                drawable = new BitmapDrawable(getResources(), bitmap1);
                double OptimizationHeight = ((double) drawable.getIntrinsicWidth()/(double) drawable.getIntrinsicHeight());
                double DownloadPhotoHeight = ((getDeviceWidth(ChooseArticle.this.getApplicationContext())) / OptimizationHeight);
                drawable.setBounds(0,0,getDeviceWidth(ChooseArticle.this.getApplicationContext()), (int) DownloadPhotoHeight);

            }
            else
            {
                countimage = countimage + 1;
                drawable = getResources().getDrawable(id);
                drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            return drawable;
        }

        public void Recreatetool(){
            Log.d("Количество картинок", String.valueOf(countimage));
            Log.d("Количество загруженных", String.valueOf(loadedcount));

            if(loadedcount == countimage) {
                recreate();
                progressBar.setVisibility(View.GONE);
            }
            if(loadedcount+1 == countimage) {

                recreate();
                progressBar.setVisibility(View.GONE);
            }
            if(loadedcount+2 == countimage) {

                recreate();
                progressBar.setVisibility(View.GONE);
            }
            if(loadedcount+3 == countimage) {

                recreate();
                progressBar.setVisibility(View.GONE);
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_article);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Установка стиля безрамочного
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
}