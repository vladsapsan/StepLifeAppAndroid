package com.example.steplifeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.steplifeapp.ui.Article;
import com.example.steplifeapp.ui.ArticleListAdapter;
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
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeArticleRedactActivity extends AppCompatActivity {


    Button SaveTopPostRedactButton;
    ImageView imagebackEditTopPost;
    private String HomeArticle_Key ="HomeArticle";
    private String Library_Key ="Lib";
    private DatabaseReference mDataBase;
    CardView CardHomeArticle1,CardHomeArticle2,CardHomeArticle3,CardHomeArticle4,CardHomeArticle5;
    ProgressBar progressBar;
    TextView LastEditText;

    private AllArticleViewModel mViewModel;
    private ImageView backbutton;
    private CardView articlecard;
    private com.example.steplifeapp.ui.ArticleListAdapter ArticleListAdapter;
    private ListView allArticlelist;
    private ArrayAdapter<String> adapter;

    private List<String> listData;
    int ChooseCard;
    private ArrayList<Article> listTemp = new ArrayList<Article>();
    Article DowArticle;
    TextView TextHomeArticle1,TextHomeArticle2,TextHomeArticle3,TextHomeArticle4,TextHomeArticle5;
    ImageView ImageHomeArticle1,ImageHomeArticle2,ImageHomeArticle3,ImageHomeArticle4,ImageHomeArticle5;
    ImageView ImageAddTop1,ImageAddTop2,ImageAddTop3,ImageAddTop4,ImageAddTop5;
    ProgressBar progressBarTopPostEdit;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;
    private String Article_Key ="AllArticle";

    ImageView imagebackEditArticles;

    //Иницилизация компонентов
    private void initilization()
    {
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(getApplicationContext(),R.layout.listviewarticleitem, listTemp);
        allArticlelist.setAdapter(ArticleListAdapter);
    }
    //Загрузка уроков из базы
    private void DownloadArticleFirebaseData()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size()>0) listData.clear();
                if(listTemp.size()>0) listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Article article = ds.getValue(Article.class);
                    //Проверка
                    assert article != null;
                    listTemp.add(article);
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
        setContentView(R.layout.activity_home_article_redact);


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


        //инициализация элементов внутри карточек 1
        TextHomeArticle1 = findViewById(R.id.TextHomeArticle1);
        ImageHomeArticle1 = findViewById(R.id.ImageHomeArticle1);
        ImageAddTop1 = findViewById(R.id.ImageAddTopHome1);

        //Плашка выбора статьи для загрузки
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheetchoosearticles,
                        (FrameLayout) findViewById(R.id.SheetDialogChooseArticleContainer)
                );

        allArticlelist = bottomSheetView.findViewById(R.id.AllArticleListview);
        //Выбор статьи в списке листа и загрузка в карточку
        allArticlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DowArticle = listTemp.get(position);
                switch (ChooseCard) {
                    case  (1):
                        imagebackEditArticles.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        ImageAddTop1.setVisibility(View.GONE);
                        bottomSheetDialog.dismiss();
                        //отображение в карточке
                        TextHomeArticle1.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                        Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle1);
                        //Загрузка статьи в базууу
                        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key);
                        mDataBase.child("1").setValue(DowArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Загрузка новой даты изменения
                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());
                                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        imagebackEditArticles.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                        break;
                    case (2):

                        break;
                    case (3):

                        break;
                    case (4):

                        break;
                    case (5):

                        break;
                    default:
                        break;
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);



        //Выбор первой карточки для загрузки туда статьи
        CardHomeArticle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseCard = 1;
            }
        });

        initilization();
        DownloadArticleFirebaseData();
        //Закрытие окна
        imagebackEditArticles = findViewById(R.id.imagebackEditHome);
        imagebackEditArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key);
        //Получение данных из базы
        mDataBase.child("LastEdit").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        LastEditText.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){

                    }

                }
            }
        });
        //Загрузка данных о 1 карточке
        mDataBase.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle =  task.getResult().getValue(Article.class);
                        if(DowArticle!= null){
                            ImageAddTop1.setVisibility(View.GONE);
                            TextHomeArticle1.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(ImageHomeArticle1);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });




    }
}