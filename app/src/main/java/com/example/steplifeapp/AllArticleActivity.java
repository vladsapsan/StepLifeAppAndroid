package com.example.steplifeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steplifeapp.other.NetworkChangeListner;
import com.example.steplifeapp.ui.Article;
import com.example.steplifeapp.ui.ArticleListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllArticleActivity extends AppCompatActivity {


    private ImageView backbutton;

    private ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private NetworkChangeListner networkChangeListner;

    private List<String> listData;
    ValueEventListener valueEventListener;
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    ProgressBar progressBar;
    Article DowArticle;
    private String Article_Key ="AllArticle";
    private DatabaseReference mDataBase;

    //Иницилизация компонентов
    private void initilization()
    {
        allArticlelist = findViewById(R.id.AllArticleListviewActivity);
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(getApplicationContext(),R.layout.listviewarticleitem, listTemp);
        allArticlelist.setAdapter(ArticleListAdapter);
    }

    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
         valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int a = 0;
                progressBar.setMax((int) snapshot.getChildrenCount());
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Article article = ds.getValue(Article.class);
                    assert article != null;
                    listTemp.add(article);
                    a++;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(a, true);
                    }
                    if(progressBar.getProgress()==(int) snapshot.getChildrenCount())
                    {

                    }
                }
                ArticleListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_article);

        // Установка стиля безрамочного
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        progressBar = findViewById(R.id.progressBarAllArticleActivity);


        //Загрузка элементов
        initilization();
        DownloadArticleFirebaseData();

        //Поиск
        EditText SearchText = findViewById(R.id.editTextSearchActivity);
        SearchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDataBase.removeEventListener(valueEventListener);
                return false;
            }
        });
        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TextAdapter", "Удален");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArticleListAdapter.getFilter().filter(s.toString());
                Log.d("TextAdapter", String.valueOf(ArticleListAdapter.getCount()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //При выборе урока переход на новый экран
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //    Bundle Bundle = new Bundle();
                DowArticle = listTemp.get(position);
                // создание объекта Intent для запуска ChooseArticle
                Intent intent = new Intent(getApplicationContext(), ChooseArticle.class);
                // передача объекта с ключом "MainText" и значением
                intent.putExtra("MainText",DowArticle.MainText);
                intent.putExtra("Date",DowArticle.Date);
                intent.putExtra("HeaderText", Html.fromHtml(DowArticle.HeadText).toString().trim());
                // запуск ChooseArticle
                startActivity(intent);
            }
        });

        //Кнопка закрытия окна
        backbutton = findViewById(R.id.BacktoNotifActivity);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}