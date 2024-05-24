package com.StepLife.steplifeapp.garbage;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.StepLife.steplifeapp.MainEnterenceActivity.MainActivity;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.Model.Article;
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
    ImageView BacktoAllActivity;
    ListView AllArticleListviewTags;
    private ArticleListAdapter ArticleListAdapter;
    private List<String> listData;

    ValueEventListener valueEventListener;
    private static final String Tags_Key ="AllTags";
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    private ArrayList <Article> listTempTags = new ArrayList<Article>();
    ProgressBar progressBarAllArticleTagsActivity;
    public static final String Article_Key ="AllArticle";
    private DatabaseReference mDataBase,mDataTags;

    //Иницилизация компонентов
    private void initilization()
    {
        AllArticleListviewTags = findViewById(R.id.AllArticleListviewTags);
        AllArticleListviewTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity.LoadArticleFragmentFromID(listTemp.get(position).id,getSupportFragmentManager(),R.id.AllArticle);
                // MainActivity.LoadArticleFragment(listTemp.get(position) ,getSupportFragmentManager(),R.id.AllArticle);
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
        TagFilter =  arguments.getString("TagFilter");
        if(TagFilter!=null) {
            TextviewTagFilter.setText(TagFilter);
            initilization();
            DownloadArticleFirebaseData();
        }
    }

}