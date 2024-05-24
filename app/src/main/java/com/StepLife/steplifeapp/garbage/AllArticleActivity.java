package com.StepLife.steplifeapp.garbage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.MainEnterenceActivity.ChooseArticle;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.MyRecyclerViewTagsAdapter;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.Model.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllArticleActivity extends AppCompatActivity implements MyRecyclerViewTagsAdapter.ItemClickListener {


    private ImageView backbutton;
    FloatingActionButton floating_action_button_AllArticle;

    private ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    CardView CardViewAllChooseTags;
    ChipGroup chipGroup;
    Button AddSortBytagButton;
    private NetworkChangeListner networkChangeListner;
    private RadioGroup EditTextRG;

    private List<String> listData;
    private List<String> ListSelectChips;
    RecyclerView AllArticleRecycleview,AllChooseArticleRecycleview;
    MyRecyclerViewTagsAdapter adapterArticleTags,adapterArticleChooseTags;
    ValueEventListener valueEventListener;
    ArrayList<String> mNewArticleTags,mChooseArticleTags;
    private static final String Tags_Key ="AllTags";
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    private ArrayList <Article> listTempTags = new ArrayList<Article>();
    ProgressBar progressBar;
    Article DowArticle;
    TextView SortTextView;
    private static final String Article_Key ="AllArticle";
    private DatabaseReference mDataBase,mDataTags;

    //Иницилизация компонентов
    private void initilization()
    {
        allArticlelist = findViewById(R.id.AllArticleListviewActivity);
        allArticlelist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                //Проверка на то , видно ли первый элемент таблицы всех статей
                if (allArticlelist != null) {
                    try {
                        if (allArticlelist.getFirstVisiblePosition()==0) {
                            //Видно
                            floating_action_button_AllArticle.hide();
                        } else {
                            floating_action_button_AllArticle.show();
                        }
                    }catch (Exception e){}
                }
            }
        });
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
                    listTempTags.add(article);
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




        progressBar = findViewById(R.id.progressBarAllArticleActivity);
        //Отображение тегов в списке новой статьи
        AllArticleRecycleview = findViewById(R.id.AllArticleRecycleview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(AllArticleActivity.this,LinearLayoutManager.HORIZONTAL, false);
        AllArticleRecycleview.setLayoutManager(layoutManager);
        mNewArticleTags = new ArrayList<>();
        adapterArticleTags = new MyRecyclerViewTagsAdapter(AllArticleActivity.this,mNewArticleTags);
        adapterArticleTags.setClickListener(AllArticleActivity.this);
        AllArticleRecycleview.setAdapter(adapterArticleTags);
        adapterArticleTags.notifyDataSetChanged();




        //группа тегов) и текст
        chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
            }
        });
        SortTextView = findViewById(R.id.SortTextView);
        AddSortBytagButton = findViewById(R.id.AddSortBytagButton);
        AddSortBytagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<mNewArticleTags.size();i++) {
                    Chip chip = new Chip(chipGroup.getContext());
                    chip.setText(mNewArticleTags.get(i).toString());
                    chip.setClickable(true);
                    chip.setCheckable(true);
                    chip.setChipStrokeColor(getResources().getColorStateList(R.color.chipselectedtextcolor));
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.bottomnavcolor));
                    chip.setTextColor(getResources().getColorStateList(R.color.chipselectedtextcolor));
                    chipGroup.addView(chip);
                }
                SortTextView.setVisibility(View.VISIBLE);
                chipGroup.setVisibility(View.VISIBLE);
                AddSortBytagButton.setVisibility(View.GONE);
            }
        });

        //Отображение выбранных! тегов в списке новой статьи
        AllChooseArticleRecycleview = findViewById(R.id.AllChooseArticleRecycleview);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(AllArticleActivity.this, LinearLayoutManager.HORIZONTAL, false);
        AllChooseArticleRecycleview.setLayoutManager(layoutManager1);

        mChooseArticleTags = new ArrayList<>();
        adapterArticleChooseTags = new MyRecyclerViewTagsAdapter(AllArticleActivity.this,mChooseArticleTags);

        AllChooseArticleRecycleview.setAdapter(adapterArticleChooseTags);
        adapterArticleChooseTags.notifyDataSetChanged();


        CardViewAllChooseTags = findViewById(R.id.CardViewAllChooseTags);



        //Загрузка элементов
        initilization();
        DownloadArticleFirebaseData();

        //Кнопка возврата к началу списка
        floating_action_button_AllArticle = findViewById(R.id.floating_action_button_AllArticle);
        floating_action_button_AllArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allArticlelist.smoothScrollToPosition(0);
            }
        });
        floating_action_button_AllArticle.hide();

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
                if(DowArticle.TagList!=null){
                    intent.putStringArrayListExtra("TagList", DowArticle.TagList);
                }
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
    //Выбор тега
    @Override
    public void onItemClick(View view, int position) {
        // mChooseArticleTags.add(mNewArticleTags.get(position));
       // mNewArticleTags.remove(position);
       // adapterArticleChooseTags.notifyDataSetChanged();
       // adapterArticleTags.notifyDataSetChanged();
        Intent intent = new Intent(AllArticleActivity.this, TagSearchArticle.class);
        intent.putExtra("TagFilter",mNewArticleTags.get(position));
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mNewArticleTags.size()==0) {
            mDataTags = FirebaseDatabase.getInstance().getReference(Tags_Key);
            mDataTags.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map = (HashMap<String, String>) task.getResult().getValue();
                    mNewArticleTags.addAll(map.values());
                    adapterArticleTags.notifyDataSetChanged();
                }
            });
        }
    }
}