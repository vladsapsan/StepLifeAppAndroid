package com.example.steplifeapp.StafFunction;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.steplifeapp.AllArticleViewModel;
import com.example.steplifeapp.R;
import com.example.steplifeapp.other.NetworkChangeListner;
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

public class TopPostRedactActivity extends AppCompatActivity {

    Button SaveTopPostRedactButton;
    NetworkChangeListner networkChangeListner;
    ImageView imagebackEditTopPost;

    private String TopPost_Key ="TopPostArticle";
    private String Library_Key ="Lib";
    private DatabaseReference mDataBase;
    CardView TopPostCard1,TopPostCard2,TopPostCard3,TopPostCard4,TopPostCard5;
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
    TextView TopPostText1,TopPostText2,TopPostText3,TopPostText4,TopPostText5;
    ImageView TopPostImage1,TopPostImage2,TopPostImage3,TopPostImage4,TopPostImage5;
    ImageView ImageAddTop1,ImageAddTop2,ImageAddTop3,ImageAddTop4,ImageAddTop5;
    ProgressBar progressBarTopPostEdit;
    Uri DownloadphotoUri;
    BottomSheetDialog bottomSheetDialog;

    private Target mTarget;
    private String Article_Key ="AllArticle";

    EditText NameTopPost,SecNameTopPost;

    //Иницилизация компонентов
    private void initilization()
    {
        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(getApplicationContext(), R.layout.listviewarticleitem, listTemp);
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
        setContentView(R.layout.activity_top_post_redact);




        //Дефолт стиль
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


        NameTopPost = findViewById(R.id.NameTopPost);
        SecNameTopPost = findViewById(R.id.SecNameTopPost);
        LastEditText = findViewById(R.id.LastEditText);
        progressBar = findViewById(R.id.progressBar);

        //инициализация карточек
        TopPostCard1 = findViewById(R.id.TopPostCard1);
        TopPostCard2 = findViewById(R.id.TopPostCard2);
        TopPostCard3 = findViewById(R.id.TopPostCard3);
        TopPostCard4 = findViewById(R.id.TopPostCard4);
        TopPostCard5 = findViewById(R.id.TopPostCard5);


        //инициализация элементов внутри карточек 1
        TopPostText1 = findViewById(R.id.TopPostText1);
        TopPostImage1 = findViewById(R.id.TopPostImage1);
        ImageAddTop1 = findViewById(R.id.ImageAddTop1);

        //инициализация элементов внутри карточек 2
        TopPostText2 = findViewById(R.id.TopPostText2);
        TopPostImage2 = findViewById(R.id.TopPostImage2);
        ImageAddTop2 = findViewById(R.id.ImageAddTop2);

        //инициализация элементов внутри карточек 3
        TopPostText3 = findViewById(R.id.TopPostText3);
        TopPostImage3 = findViewById(R.id.TopPostImage3);
        ImageAddTop3 = findViewById(R.id.ImageAddTop3);

        //инициализация элементов внутри карточек 4
        TopPostText4 = findViewById(R.id.TopPostText4);
        TopPostImage4 = findViewById(R.id.TopPostImage4);
        ImageAddTop4 = findViewById(R.id.ImageAddTop4);

        //инициализация элементов внутри карточек 5
        TopPostText5 = findViewById(R.id.TopPostText5);
        TopPostImage5 = findViewById(R.id.TopPostImage5);
        ImageAddTop5 = findViewById(R.id.ImageAddTop5);


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
                        progressBar.setVisibility(View.VISIBLE);
                        imagebackEditTopPost.setVisibility(View.GONE);
                        ImageAddTop1.setVisibility(View.GONE);
                        bottomSheetDialog.dismiss();
                        //отображение в карточке
                        TopPostText1.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                        Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage1);
                        //Загрузка статьи в базууу
                        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
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
                                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                        break;
                    case (2):
                        progressBar.setVisibility(View.VISIBLE);
                        imagebackEditTopPost.setVisibility(View.GONE);
                        ImageAddTop2.setVisibility(View.GONE);
                        bottomSheetDialog.dismiss();
                        //отображение в карточке
                        TopPostText2.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                        Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage2);
                        //Загрузка статьи в базууу
                        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
                        mDataBase.child("2").setValue(DowArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Загрузка новой даты изменения
                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());
                                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                        break;
                    case (3):
                        progressBar.setVisibility(View.VISIBLE);
                        imagebackEditTopPost.setVisibility(View.GONE);
                        ImageAddTop3.setVisibility(View.GONE);
                        bottomSheetDialog.dismiss();
                        //отображение в карточке
                        TopPostText3.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                        Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage3);
                        //Загрузка статьи в базууу
                        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
                        mDataBase.child("3").setValue(DowArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Загрузка новой даты изменения
                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());
                                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                        break;
                    case (4):
                        progressBar.setVisibility(View.VISIBLE);
                        imagebackEditTopPost.setVisibility(View.GONE);
                        ImageAddTop4.setVisibility(View.GONE);
                        bottomSheetDialog.dismiss();
                        //отображение в карточке
                        TopPostText4.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                        Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage4);
                        //Загрузка статьи в базууу
                        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
                        mDataBase.child("4").setValue(DowArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Загрузка новой даты изменения
                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());
                                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                        break;
                    case (5):
                        progressBar.setVisibility(View.VISIBLE);
                        imagebackEditTopPost.setVisibility(View.GONE);
                        ImageAddTop5.setVisibility(View.GONE);
                        bottomSheetDialog.dismiss();
                        //отображение в карточке
                        TopPostText5.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                        Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage5);
                        //Загрузка статьи в базууу
                        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
                        mDataBase.child("5").setValue(DowArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Загрузка новой даты изменения
                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());
                                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);

        //Выбор первой карточки для загрузки туда статьи
        TopPostCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseCard = 1;
            }
        });

        TopPostCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseCard = 2;
            }
        });

        TopPostCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseCard = 3;
            }
        });

        TopPostCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseCard = 4;
            }
        });

        TopPostCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                ChooseCard = 5;
            }
        });

        //закрытие окна
        imagebackEditTopPost = findViewById(R.id.imagebackEditTopPost);
        imagebackEditTopPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Сохранение изменений
        SaveTopPostRedactButton = findViewById(R.id.SaveTopPostRedactButton);
        SaveTopPostRedactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
                progressBar.setVisibility(View.VISIBLE);
                imagebackEditTopPost.setVisibility(View.GONE);

                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                String Simpledate = df.format(Calendar.getInstance().getTime());
                mDataBase.child("Name").setValue(NameTopPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mDataBase.child("SecName").setValue(SecNameTopPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();



        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
        //Получение данных из базы
        mDataBase.child("Name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        NameTopPost.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){

                    }

                }
            }
        });

        //Получение данных из базы
        mDataBase.child("SecName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        SecNameTopPost.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){

                    }
                }
            }
        });
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
                        initilization();
                        DownloadArticleFirebaseData();
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
                            TopPostText1.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage1);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 2 карточке
        mDataBase.child("2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            ImageAddTop2.setVisibility(View.GONE);
                            TopPostText2.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage2);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 3 карточке
        mDataBase.child("3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            ImageAddTop3.setVisibility(View.GONE);
                            TopPostText3.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage3);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 4 карточке
        mDataBase.child("4").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            ImageAddTop4.setVisibility(View.GONE);
                            TopPostText4.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage4);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        //Загрузка данных о 5 карточке
        mDataBase.child("5").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            ImageAddTop5.setVisibility(View.GONE);
                            TopPostText5.setText(Html.fromHtml(DowArticle.HeadText).toString().trim());
                            Glide.with(getApplicationContext()).load(DowArticle.PreviewPhotoUri).into(TopPostImage5);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });


    }



    @Override
    protected void onStop() {
        super.onStop();

    }
}