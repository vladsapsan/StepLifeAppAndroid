package com.StepLife.steplifeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.other.MyRecyclerViewTagsAdapter;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TagSearchArticle extends AppCompatActivity {

    TextView TextviewTagFilter;
    String TagFilter;
    CardView BacktoAllActivity;
    ListView AllArticleListviewTags;

    private ImageView backbutton;

    private ArticleListAdapter ArticleListAdapter;
    CardView CardViewAllChooseTags;
    private NetworkChangeListner networkChangeListner;

    private List<String> listData;
    RecyclerView AllArticleRecycleview,AllChooseArticleRecycleview;
    MyRecyclerViewTagsAdapter adapterArticleTags,adapterArticleChooseTags;
    ValueEventListener valueEventListener;
    ArrayList<String> mNewArticleTags,mChooseArticleTags;
    private static final String Tags_Key ="AllTags";
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    private ArrayList <Article> listTempTags = new ArrayList<Article>();
    ProgressBar progressBarAllArticleTagsActivity;
    private static final String Article_Key ="AllArticle";
    private DatabaseReference mDataBase,mDataTags;

    //Иницилизация компонентов
    private void initilization()
    {
        AllArticleListviewTags = findViewById(R.id.AllArticleListviewTags);
        AllArticleListviewTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article article = listTemp.get(position);
                // создание объекта Intent для запуска ChooseArticle
                Intent intent = new Intent(TagSearchArticle.this, ChooseArticle.class);
                // передача объекта с ключом "MainText" и значением
                intent.putExtra("MainText",article.MainText);
                intent.putExtra("Date",article.Date);
                intent.putExtra("HeaderText", Html.fromHtml(article.HeadText).toString().trim());
                if(article.TagList!=null){
                    intent.putStringArrayListExtra("TagList", article.TagList);
                }
                // запуск ChooseArticle
                startActivity(intent);
                finish();
            }
        });


        progressBarAllArticleTagsActivity = findViewById(R.id.progressBarAllArticleTagsActivity);
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(TagSearchArticle.this,R.layout.listviewarticleitem, listTemp);
        AllArticleListviewTags.setAdapter(ArticleListAdapter);
    }

    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int a = 0;
                progressBarAllArticleTagsActivity.setMax((int) snapshot.getChildrenCount());
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Article article = ds.getValue(Article.class);
                    assert article != null;
                    if(article.TagList!=null) {
                        Log.d("Datacheck", String.valueOf(article.TagList.size()));
                        for (String ArticleTag : article.TagList) {
                            Log.d("Datacheck", ArticleTag);
                            Log.d("Datacheck1", TagFilter);
                            if (ArticleTag.equals(TagFilter)) {
                                listTemp.add(article);
                                Log.d("Datacheck", "NewArticle");
                            }
                        }
                    }
                    a++;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBarAllArticleTagsActivity.setProgress(a, true);
                    }
                    if(progressBarAllArticleTagsActivity.getProgress()==(int) snapshot.getChildrenCount())
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
        setContentView(R.layout.activity_tag_search_article);




        TextviewTagFilter = findViewById(R.id.TextviewTagFilter);
        //Кнопка выхода
        BacktoAllActivity = findViewById(R.id.BacktoAllActivity);
        BacktoAllActivity.setOnClickListener(new View.OnClickListener() {
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
        TagFilter = (String) arguments.get("TagFilter");
        if(TagFilter!=null) {
            TextviewTagFilter.setText(TagFilter);
            initilization();
            DownloadArticleFirebaseData();
        }
    }

}