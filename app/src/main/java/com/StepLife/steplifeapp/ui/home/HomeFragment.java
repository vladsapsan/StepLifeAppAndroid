package com.StepLife.steplifeapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.AllArticleActivity;
import com.StepLife.steplifeapp.ChooseArticle;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.UserProfile.User_ProfileActiviti;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FrameLayout FrameVideo;
    TextView TextBtnHide,AllAcricleButton;
    private String HomeArticle_Key ="HomeArticle";
    private String Library_Key ="Lib";
    Article DowArticle1,DowArticle2,DowArticle3,DowArticle4,DowArticle5;
    CardView CardHomeArticle1,CardHomeArticle2,CardHomeArticle3,CardHomeArticle4,CardHomeArticle5;
    TextView TextHomeArticle1,TextHomeArticle2,TextHomeArticle3,TextHomeArticle4,TextHomeArticle5;
    ImageView ImageHomeArticle1,ImageHomeArticle2,ImageHomeArticle3,ImageHomeArticle4,ImageHomeArticle5;
    List<String> HomeTopArticleList;
    CardView ImageProfile,HowToGetProtCard;
    final private static String DBase_Code = "AllArticle";
    final private static String DBase_HomeTopArticleCode = "HomeTopArticle";
    final private static String DB_Article_HowToGet = "-NJgrzWOZOFxEejjLr5J";
    private DatabaseReference mDatabase;
    private ArrayList<Article> listTemp = new ArrayList<Article>();
    Button buttonConnect;
    private ArticleListAdapter ArticleListAdapter;
    CardView  ArticleTeach;
    ScrollView HomescrollView;
    RecyclerView HomeArticleListView;
    HorizontalScrollView HomeArticleScroll;
    private ImageView ArticleState1,ArticleState2,ArticleState3;
    private FirebaseAuth mAuth;
    HorizontalScrollView horizontalScrollViewArticle,horizontalScrollView2;
    private Animation HideAnimation;
    private HomeViewModel homeViewModel;
    private ListView allArticlelist;
    Animation animationClick;
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDataBase;
    int CurrnetPositionList ;
    private List<String> listData;
    Animation animationIN,animationUP;
    private String Article_Key ="AllArticle";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //Иницилизация компонентов
    private void initilization()
    {

        listData = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference(Article_Key);
        ArticleListAdapter = new ArticleListAdapter(getContext(),R.layout.listviewhomearticle, listTemp);
      //  HomeArticleListView.setAdapter(ArticleListAdapter);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();
        Intent intentAllArticle = new Intent(getActivity(), AllArticleActivity.class);
        Intent User_ProfileActiviti = new Intent(getActivity(), User_ProfileActiviti.class);


        HomeArticleScroll = view.findViewById(R.id.HomeArticleScroll);



        //Инициализация анимации
        animationIN = AnimationUtils.loadAnimation(getContext(),R.anim.expected_home_fragment);
        animationUP = AnimationUtils.loadAnimation(getContext(),R.anim.expected_app_bar);


        //инициализация карточек
        CardHomeArticle1 = view.findViewById(R.id.CardHomeArticle1);
        CardHomeArticle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle1!=null) {
                    Intent intentChooseArticle = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intentChooseArticle.putExtra("MainText", DowArticle1.MainText);
                    intentChooseArticle.putExtra("Date", DowArticle1.Date);
                    intentChooseArticle.putExtra("HeaderText", Html.fromHtml(DowArticle1.HeadText).toString().trim());
                    if(DowArticle1.TagList!=null){
                        intentChooseArticle.putStringArrayListExtra("TagList", DowArticle1.TagList);
                    }
                    // запуск ChooseArticle
                    startActivity(intentChooseArticle);
                }
            }
        });
        CardHomeArticle2 = view.findViewById(R.id.CardHomeArticle2);
        CardHomeArticle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle2!=null) {
                    Intent intentChooseArticle = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intentChooseArticle.putExtra("MainText", DowArticle2.MainText);
                    intentChooseArticle.putExtra("Date", DowArticle2.Date);
                    intentChooseArticle.putExtra("HeaderText", Html.fromHtml(DowArticle2.HeadText).toString().trim());
                    if(DowArticle2.TagList!=null){
                        intentChooseArticle.putStringArrayListExtra("TagList", DowArticle2.TagList);
                    }
                    // запуск ChooseArticle
                    startActivity(intentChooseArticle);
                }
            }
        });
        CardHomeArticle3 = view.findViewById(R.id.CardHomeArticle3);
        CardHomeArticle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle3!=null) {
                    Intent intentChooseArticle = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intentChooseArticle.putExtra("MainText", DowArticle3.MainText);
                    intentChooseArticle.putExtra("Date", DowArticle3.Date);
                    intentChooseArticle.putExtra("HeaderText", Html.fromHtml(DowArticle3.HeadText).toString().trim());
                    if(DowArticle3.TagList!=null){
                        intentChooseArticle.putStringArrayListExtra("TagList", DowArticle3.TagList);
                    }
                    // запуск ChooseArticle
                    startActivity(intentChooseArticle);
                }
            }
        });
        CardHomeArticle4 = view.findViewById(R.id.CardHomeArticle4);
        CardHomeArticle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle4!=null) {
                    Intent intentChooseArticle = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intentChooseArticle.putExtra("MainText", DowArticle4.MainText);
                    intentChooseArticle.putExtra("Date", DowArticle4.Date);
                    intentChooseArticle.putExtra("HeaderText", Html.fromHtml(DowArticle4.HeadText).toString().trim());
                    if(DowArticle4.TagList!=null){
                        intentChooseArticle.putStringArrayListExtra("TagList", DowArticle4.TagList);
                    }
                    // запуск ChooseArticle
                    startActivity(intentChooseArticle);
                }
            }
        });
        CardHomeArticle5 = view.findViewById(R.id.CardHomeArticle5);
        CardHomeArticle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle5!=null) {
                    Intent intentChooseArticle = new Intent(getActivity(), ChooseArticle.class);
                    // передача объекта с ключом "MainText" и значением
                    intentChooseArticle.putExtra("MainText", DowArticle5.MainText);
                    intentChooseArticle.putExtra("Date", DowArticle5.Date);
                    intentChooseArticle.putExtra("HeaderText", Html.fromHtml(DowArticle5.HeadText).toString().trim());
                    if(DowArticle5.TagList!=null){
                        intentChooseArticle.putStringArrayListExtra("TagList", DowArticle5.TagList);
                    }
                    // запуск ChooseArticle
                    startActivity(intentChooseArticle);
                }
            }
        });

        //инициализация элементов внутри карточек 1
        TextHomeArticle1 = view.findViewById(R.id.TextHomeArticle1);
        ImageHomeArticle1 = view.findViewById(R.id.ImageHomeArticle1);

        //инициализация элементов внутри карточек 2
        TextHomeArticle2 = view.findViewById(R.id.TextHomeArticle2);
        ImageHomeArticle2 = view.findViewById(R.id.ImageHomeArticle2);

        //инициализация элементов внутри карточек 3
        TextHomeArticle3 = view.findViewById(R.id.TextHomeArticle3);
        ImageHomeArticle3 = view.findViewById(R.id.ImageHomeArticle3);
        //инициализация элементов внутри карточек 4
        TextHomeArticle4 = view.findViewById(R.id.TextHomeArticle4);
        ImageHomeArticle4 = view.findViewById(R.id.ImageHomeArticle4);
        //инициализация элементов внутри карточек 5
        TextHomeArticle5 = view.findViewById(R.id.TextHomeArticle5);
        ImageHomeArticle5 = view.findViewById(R.id.ImageHomeArticle5);


        //Переход ко всем статьям
        AllAcricleButton = (TextView) view.findViewById(R.id.AllAcricleButton);
        AllAcricleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAllArticle);
            }
        });


        //Кнопка перехода к подключению модуля
        buttonConnect = view.findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //кнопка закрытия видео на главном экране
        FrameVideo = (FrameLayout) view.findViewById(R.id.FrameVideoInstruction);
        TextBtnHide = (TextView) view.findViewById(R.id.textHideVideoButton);
        TextBtnHide.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //запуск анимации
                FrameVideo.setVisibility(View.GONE);
            }

        });

        //Картинки статей на главном экране
    //    ArticleState1 =  view.findViewById(R.id.ArticleState1);
    //    ArticleState2 =  view.findViewById(R.id.ArticleState2);
     //   ArticleState3 =  view.findViewById(R.id.ArticleState3);


    }
    //Скрытие Видео по нажатию на текст

    @Override
    public void onStart() {
        super.onStart();
       // FirebaseUser cUser = mAuth.getCurrentUser();
       // if(cUser!=null)
       // {
        //Запуск анимации при старте
        HomeArticleScroll.setAnimation(animationIN);
        FrameVideo.setAnimation(animationUP);
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key);
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
                        DowArticle1 =  task.getResult().getValue(Article.class);
                        if(DowArticle1!= null){
                            TextHomeArticle1.setText(Html.fromHtml(DowArticle1.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle1.PreviewPhotoUri).into(ImageHomeArticle1);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        mDataBase.child("2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle2 =  task.getResult().getValue(Article.class);
                        if(DowArticle2!= null){
                            TextHomeArticle2.setText(Html.fromHtml(DowArticle2.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle2.PreviewPhotoUri).into(ImageHomeArticle2);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        mDataBase.child("3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle3 =  task.getResult().getValue(Article.class);
                        if(DowArticle3!= null){
                            TextHomeArticle3.setText(Html.fromHtml(DowArticle3.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle3.PreviewPhotoUri).into(ImageHomeArticle3);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        mDataBase.child("4").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle4 =  task.getResult().getValue(Article.class);
                        if(DowArticle4!= null){
                            TextHomeArticle4.setText(Html.fromHtml(DowArticle4.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle4.PreviewPhotoUri).into(ImageHomeArticle4);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        mDataBase.child("5").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        DowArticle5 =  task.getResult().getValue(Article.class);
                        if(DowArticle5!= null){
                            TextHomeArticle5.setText(Html.fromHtml(DowArticle5.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle5.PreviewPhotoUri).into(ImageHomeArticle5);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });
       //    String phoneNumber = cUser.getPhoneNumber();
       //     Uri UriPhoto = cUser.getPhotoUrl();

      //  }
    }

}