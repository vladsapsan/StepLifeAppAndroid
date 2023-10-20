package com.StepLife.steplifeapp.StafFunction;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.AllArticleViewModel;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.other.Section;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.ui.Article;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeArticleRedactActivity extends AppCompatActivity implements SectionArticleViewAdapter.ItemClickListener {


    Button SaveTopPostRedactButton,SelectSectionHomeButton,SelectSectionHomeButton2;
    ImageView imagebackEditTopPost;
    TextView ChooseTextView,TextviewSectionName1,TextviewSectionName2;

    private String HomeArticle_Key ="HomeArticle";
    private String Library_Key ="Lib";
    RecyclerView RecycleviewSectionArticle;
    SectionArticleViewAdapter sectionArticleViewAdapter;
    private DatabaseReference mDataBase;
    CardView CardHomeArticle1,CardHomeArticle2,CardHomeArticle3,CardHomeArticle4,CardHomeArticle5;
    ProgressBar progressBar;
    public static final String Section_Article_Key ="AllArticleSection";
    public static final String Section1_Article_Key ="Section1";
    public static final String Section2_Article_Key ="Section2";
    public static final String Section3_Article_Key ="Section3";
    TextView LastEditText;

    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private com.StepLife.steplifeapp.ui.ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private ArrayAdapter<String> adapter;

    private List<String> listData;
    int ChooseSection;
    private ArrayList<String> listTemp = new ArrayList<>();
    private ArrayList<Section> SectionlistTemp = new ArrayList<>();
    private ArrayList<Article> ArticlelistTemp = new ArrayList<>();
    Article DowArticle;
    NetworkChangeListner networkChangeListner;
    TextView TextHomeArticle1,TextHomeArticle2,TextHomeArticle3,TextHomeArticle4,TextHomeArticle5;
    ImageView ImageHomeArticle1,ImageHomeArticle2,ImageHomeArticle3,ImageHomeArticle4,ImageHomeArticle5;
    ImageView ImageAddTop1,ImageAddTop2,ImageAddTop3,ImageAddTop4,ImageAddTop5;
    ProgressBar progressBarTopPostEdit;
    Uri DownloadphotoUri;
    ArrayAdapter<String> SectionAdapter;
    BottomSheetDialog bottomSheetDialog;
    private String Article_Key ="AllArticle";
    ImageView imagebackEditArticles;

    //Иницилизация компонентов
    private void initilization()
    {
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Section_Article_Key);
        SectionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listTemp);
        allArticlelist.setAdapter(SectionAdapter);

        //Второй столбец данных
        sectionArticleViewAdapter = new SectionArticleViewAdapter(HomeArticleRedactActivity.this,ArticlelistTemp);
        RecycleviewSectionArticle.setAdapter(sectionArticleViewAdapter);
    }
    //Загрузка разделов из базы
    private void DownloadSectionFirebaseData()
    {
        mDataBase = FirebaseDatabase.getInstance().getReference(Section_Article_Key);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Section section = ds.getValue(Section.class);
                    //Проверка
                    assert section != null;
                    listTemp.add(section.SectionName);
                    SectionlistTemp.add(section);
                }
                SectionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }
    private void DownloadArticleFirebaseData(DatabaseReference mDataBase)
    {
        ArticlelistTemp.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(ArticlelistTemp.size()>0) ArticlelistTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Article article = ds.getValue(Article.class);
                    //Проверка
                    assert article != null;
                    ArticlelistTemp.add(article);
                }
                sectionArticleViewAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_home_article_redact);


        //Дефолт стиль
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        //Аунтефикация
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUser = mAuth.getCurrentUser();


        //инициализация карточек
        CardHomeArticle1 = findViewById(R.id.CardHomeArticle1);
        CardHomeArticle2 = findViewById(R.id.CardHomeArticle2);
        CardHomeArticle3 = findViewById(R.id.CardHomeArticle3);
        CardHomeArticle4 = findViewById(R.id.CardHomeArticle4);
        CardHomeArticle5 = findViewById(R.id.CardHomeArticle5);

        progressBar = findViewById(R.id.progressBar);
        LastEditText = findViewById(R.id.LastEditText);
        TextviewSectionName1 = findViewById(R.id.TextviewSectionName1);
        TextviewSectionName2 = findViewById(R.id.TextviewSectionName2);
        RecycleviewSectionArticle = findViewById(R.id.RecycleviewSectionArticle);
        LinearLayoutManager layoutManager= new LinearLayoutManager(HomeArticleRedactActivity.this,LinearLayoutManager.HORIZONTAL, false);
        RecycleviewSectionArticle.setLayoutManager(layoutManager);

        //инициализация элементов внутри карточек 1
        TextHomeArticle1 = findViewById(R.id.TextHomeArticle1);
        ImageHomeArticle1 = findViewById(R.id.ImageHomeArticle1);
        ImageAddTop1 = findViewById(R.id.ImageAddTopHome1);


        //инициализация элементов внутри карточек 2
        TextHomeArticle2 = findViewById(R.id.TextHomeArticle2);
        ImageHomeArticle2 = findViewById(R.id.ImageHomeArticle2);
        ImageAddTop2 = findViewById(R.id.ImageAddTopHome2);

        //инициализация элементов внутри карточек 3
        TextHomeArticle3 = findViewById(R.id.TextHomeArticle3);
        ImageHomeArticle3 = findViewById(R.id.ImageHomeArticle3);
        ImageAddTop3 = findViewById(R.id.ImageAddTopHome3);

        //инициализация элементов внутри карточек 4
        TextHomeArticle4 = findViewById(R.id.TextHomeArticle4);
        ImageHomeArticle4 = findViewById(R.id.ImageHomeArticle4);
        ImageAddTop4 = findViewById(R.id.ImageAddTopHome4);

        //инициализация элементов внутри карточек 5
        TextHomeArticle5 = findViewById(R.id.TextHomeArticle5);
        ImageHomeArticle5 = findViewById(R.id.ImageHomeArticle5);
        ImageAddTop5 = findViewById(R.id.ImageAddTopHome5);

        //Плашка выбора раздела для загрузки
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheetchoosearticles,
                        (FrameLayout) findViewById(R.id.SheetDialogChooseArticleContainer)
                );

        ChooseTextView = bottomSheetView.findViewById(R.id.ChooseTextView);
        ChooseTextView.setText("Выберите раздел для загрузки");
        allArticlelist = bottomSheetView.findViewById(R.id.AllArticleListview);
        //Выбор статьи в списке листа и загрузка в карточку
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bottomSheetDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                imagebackEditArticles.setVisibility(View.GONE);
                if(ChooseSection==1){
                    //Загружаем выбранный раздел в бд
                    mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child(Section1_Article_Key);
                    mDataBase.setValue(SectionlistTemp.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DownloadSection1();
                                Toast.makeText(HomeArticleRedactActivity.this,"Раздел успешно загружен",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                imagebackEditArticles.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(HomeArticleRedactActivity.this,"Ошибка загрузки раздела",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                imagebackEditArticles.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else if (ChooseSection==2) {
                    //Загружаем выбранный раздел в бд
                    mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child(Section2_Article_Key);
                    mDataBase.setValue(SectionlistTemp.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DownloadSection2();
                                Toast.makeText(HomeArticleRedactActivity.this,"Раздел успешно загружен",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                imagebackEditArticles.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(HomeArticleRedactActivity.this,"Ошибка загрузки раздела",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                imagebackEditArticles.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);



        //Кнопка выбора раздела
        SelectSectionHomeButton = findViewById(R.id.SelectSectionHomeButton);
        SelectSectionHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseSection=1;
            }
        });
        SelectSectionHomeButton2 = findViewById(R.id.SelectSectionHomeButton2);
        SelectSectionHomeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseSection=2;
            }
        });
        //Выбор первой карточки для загрузки туда статьи
        CardHomeArticle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        //Выбор первой карточки для загрузки туда статьи
        CardHomeArticle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        //Выбор первой карточки для загрузки туда статьи
        CardHomeArticle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        //Выбор первой карточки для загрузки туда статьи
        CardHomeArticle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        //Выбор первой карточки для загрузки туда статьи
        CardHomeArticle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        initilization();
        DownloadSectionFirebaseData();

        //Закрытие окна
        imagebackEditArticles = findViewById(R.id.imagebackEditHome);
        imagebackEditArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
    }


    private void DownloadSection2(){
        DownloadArticleFirebaseData(FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child(Section2_Article_Key).child("articleList"));
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child(Section2_Article_Key);
        mDataBase.child("SectionName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    TextviewSectionName2.setText(task.getResult().getValue().toString());
                }
            }
        });
    }
    private void DownloadSection1(){
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child(Section1_Article_Key);
        //Получение данных из базы
        mDataBase.child("SectionName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    TextviewSectionName1.setText(task.getResult().getValue().toString());
                }
            }
        });
        //Загрузка данных о 1 карточке
        mDataBase.child("articleList").child("0").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    if(task.getResult().getValue()!=null) {
                        CardHomeArticle2.setVisibility(View.VISIBLE);
                        try {
                            //Данные получены
                            DowArticle = task.getResult().getValue(Article.class);
                            if (DowArticle != null) {
                                ImageAddTop1.setVisibility(View.GONE);
                                TextHomeArticle1.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                                Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle1);
                            }
                        } catch (Exception e) {
                        }
                    }else {
                        CardHomeArticle2.setVisibility(View.GONE);
                    }
                }
            }
        });

        //Загрузка данных о 2 карточке
        mDataBase.child("articleList").child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    if(task.getResult().getValue()!=null) {
                        CardHomeArticle2.setVisibility(View.VISIBLE);
                        try {
                            //Данные получены
                            DowArticle = task.getResult().getValue(Article.class);
                            if (DowArticle != null) {
                                ImageAddTop2.setVisibility(View.GONE);
                                TextHomeArticle2.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                                Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle2);
                            }
                        } catch (Exception e) {
                        }
                    }else {
                        CardHomeArticle2.setVisibility(View.GONE);
                    }
                }
            }
        });

        //Загрузка данных о 3 карточке
        mDataBase.child("articleList").child("2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    if(task.getResult().getValue()!=null) {
                        CardHomeArticle3.setVisibility(View.VISIBLE);
                        try {
                            //Данные получены
                            DowArticle = task.getResult().getValue(Article.class);
                            if (DowArticle != null) {
                                ImageAddTop3.setVisibility(View.GONE);
                                TextHomeArticle3.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                                Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle3);
                            }
                        } catch (Exception e) {
                        }
                    }else {
                        CardHomeArticle3.setVisibility(View.GONE);
                    }
                }
            }
        });

        //Загрузка данных о 4 карточке
        mDataBase.child("articleList").child("3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    if(task.getResult().getValue()!=null) {
                        CardHomeArticle4.setVisibility(View.VISIBLE);
                        try {
                            //Данные получены
                            DowArticle = task.getResult().getValue(Article.class);
                            if (DowArticle != null) {
                                ImageAddTop4.setVisibility(View.GONE);
                                TextHomeArticle4.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                                Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle4);
                            }
                        } catch (Exception e) {
                        }
                    }else {
                        CardHomeArticle4.setVisibility(View.GONE);
                    }
                }
            }
        });

        //Загрузка данных о 5 карточке
        mDataBase.child("articleList").child("4").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    if(task.getResult().getValue()!=null) {
                        CardHomeArticle5.setVisibility(View.VISIBLE);
                        try {
                            //Данные получены
                            DowArticle = task.getResult().getValue(Article.class);
                            if (DowArticle != null) {
                                ImageAddTop5.setVisibility(View.GONE);
                                TextHomeArticle5.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                                Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle5);
                            }
                        } catch (Exception e) {
                        }
                    }else {
                        CardHomeArticle5.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        DownloadSection1();
        DownloadSection2();
    }



    @Override
    protected void onStop() {
        super.onStop();

    }

    //Нажатие по элементу списка recycleview
    @Override
    public void onItemClick(View view, int position) {

    }
}