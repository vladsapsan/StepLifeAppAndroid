package com.StepLife.steplifeapp.ui.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.AllArticleActivity;
import com.StepLife.steplifeapp.MainActivity;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.TelephoneSign.TelephoneSignUp;
import com.StepLife.steplifeapp.UserProfile.User_ProfileActiviti;
import com.StepLife.steplifeapp.other.SectionArticleViewAdapter;
import com.StepLife.steplifeapp.ui.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.StepLife.steplifeapp.ui.dashboard.DashboardFragment;
import com.StepLife.steplifeapp.ui.notifications.NotificationsFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import aglibs.loading.skeleton.layout.SkeletonLinearLayout;


public class HomeFragment extends Fragment implements SectionArticleViewAdapter.ItemClickListener{
    FrameLayout FrameVideo;
    TextView TextBtnHide,AllAcricleButton,TextviewSectionName1,TextviewSectionName2;
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
    private static final String Section_Article_Key ="AllArticleSection";
    final private static String DB_Article_HowToGet = "-NJgrzWOZOFxEejjLr5J";
    private DatabaseReference mDatabase;
    private ArrayList<Article> listTemp = new ArrayList<Article>();
    Button buttonConnect;
    private ArticleListAdapter ArticleListAdapter;
    CardView  ArticleTeach;
    ScrollView HomescrollView;
    RecyclerView HomeArticleListView;
    SkeletonLinearLayout SceletonCardArticleSection,CardArticleSection2;
    LinearLayout ShoolStepButton;
    HorizontalScrollView HomeArticleScroll;
    //Фрагмент школа Ходьбы
    NotificationsFragment notificationsFragment;
    private ImageView ArticleState1,ArticleState2,ArticleState3,VideoStartButton,BackgroundImageVideo;
    private FirebaseAuth mAuth;
    HorizontalScrollView horizontalScrollViewArticle,horizontalScrollView2;
    private Animation HideAnimation;
    private HomeViewModel homeViewModel;
    DashboardFragment dashboardFragment;
    private ListView allArticlelist;

    final static String HomeVideoUri = "/SupportFiles/Steplife P5.mp4";

    Animation animationClick;
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDataBase;
    CardView ProfileUserButton;
    ImageView imageviewprofile;
    int CurrnetPositionList ;
    RecyclerView RecycleviewSectionArticle2;
    MediaController MediaController ;
    ArrayList <Article> ArticlelistTemp = new ArrayList<>();
    VideoView HomeVideoView;
    SectionArticleViewAdapter sectionArticleViewAdapter;
    private List<String> listData;
    String SectionID,Section1ID,Section2ID;
    Animation animationIN;
    private String Article_Key ="AllArticle";
    public final static String Bundle_Section_Tag ="SectionInfo";
    private MainActivity mainActivity;

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
    private void FireBaseVideoStart(){
        //Получение ссылки и запуск видео
        VideoStartButton.setVisibility(View.GONE);
        FirebaseStorage.getInstance().getReference().child(HomeVideoUri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                HomeVideoView.setVideoURI((downloadUrl));
                HomeVideoView.start();
            }
        });
    }
    private void VideoStart(){
        //Получение ссылки и запуск видео
        VideoStartButton.setVisibility(View.GONE);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.steplifevideo;
               HomeVideoView.setVideoURI(Uri.parse((path)));
                HomeVideoView.start();
    }



    //Загрузка раздела из базы
    private void DownloadArticleFirebaseData(DatabaseReference mDataBase)
    {
        ArticlelistTemp.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(ArticlelistTemp.size()>0) ArticlelistTemp.clear();
                int count = 0;
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    if(count>=5){
                        break;
                    }
                    Article article = ds.getValue(Article.class);
                    //Проверка
                    assert article != null;
                    ArticlelistTemp.add(article);
                    count++;
                }
                sectionArticleViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }

    MediaPlayer.OnCompletionListener myVideoViewCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer arg0) {
            VideoStartButton.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //анимация
        setExitTransition(new MaterialFadeThrough());
        setEnterTransition(new MaterialFadeThrough());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);


        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();
        Intent intentAllArticle = new Intent(getActivity(), AllArticleActivity.class);
        Intent User_ProfileActiviti = new Intent(getActivity(), User_ProfileActiviti.class);
        mainActivity = (MainActivity) getActivity();
         

        HomeArticleScroll = view.findViewById(R.id.HomeArticleScroll);

        RecycleviewSectionArticle2 = view.findViewById(R.id.RecycleviewSectionArticle2);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        RecycleviewSectionArticle2.setLayoutManager(layoutManager);
        sectionArticleViewAdapter = new SectionArticleViewAdapter(getContext(),ArticlelistTemp);
        sectionArticleViewAdapter.setClickListener(this::onItemClick);
        RecycleviewSectionArticle2.setAdapter(sectionArticleViewAdapter);





        //Инициализация анимации
        animationIN = AnimationUtils.loadAnimation(getContext(),R.anim.expected_home_fragment);


        //Видео плеер окна
        HomeVideoView = view.findViewById(R.id.HomeVideoView);
        HomeVideoView.setOnCompletionListener(myVideoViewCompletionListener);
        MediaController = new MediaController(getContext());
       // HomeVideoView.setMediaController(MediaController);
        VideoStartButton  = view.findViewById(R.id.VideoStartButton);
        VideoStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoStartButton.setVisibility(View.GONE);
                        HomeVideoView.start();
            }
        });



        //Кнопка профиля
        imageviewprofile = view.findViewById(R.id.imageviewprofile);
        ProfileUserButton = view.findViewById(R.id.ProfileUserButton);
        ProfileUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser cUser = mAuth.getCurrentUser();
                if(cUser!=null)
                {
                    startActivity(User_ProfileActiviti);
                }
                else
                {
                    startActivity(new Intent(getActivity(), TelephoneSignUp.class));
                }
            }
        });

        //Название раздела
        TextviewSectionName1 = view.findViewById(R.id.TextviewSectionName1);
        //Карточка раздела 1
        SceletonCardArticleSection = view.findViewById(R.id.SceletonCardArticleSection);
        SceletonCardArticleSection.startLoading();
        SceletonCardArticleSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Section1ID!=null) {
                    MainActivity.LoadSectionFragment(Section1ID,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        //Карточка раздела 1
        CardArticleSection2 = view.findViewById(R.id.CardArticleSection2);
        CardArticleSection2.startLoading();
        CardArticleSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Section2ID!=null) {
                    MainActivity.LoadSectionFragment(Section2ID,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        //инициализация карточек
        CardHomeArticle1 = view.findViewById(R.id.CardHomeArticle1);
        CardHomeArticle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle1!=null) {
                    MainActivity.LoadArticleFragment(DowArticle1 ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        CardHomeArticle2 = view.findViewById(R.id.CardHomeArticle2);
        CardHomeArticle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle2!=null) {
                    MainActivity.LoadArticleFragment(DowArticle2 ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        CardHomeArticle3 = view.findViewById(R.id.CardHomeArticle3);
        CardHomeArticle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle3!=null) {
                    MainActivity.LoadArticleFragment(DowArticle3 ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        CardHomeArticle4 = view.findViewById(R.id.CardHomeArticle4);
        CardHomeArticle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle4!=null) {
                    MainActivity.LoadArticleFragment(DowArticle4 ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
                }
            }
        });
        CardHomeArticle5 = view.findViewById(R.id.CardHomeArticle5);
        CardHomeArticle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DowArticle5!=null) {
                    MainActivity.LoadArticleFragment(DowArticle5 ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
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
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.findFragmentByTag("2")!=null) {
                    dashboardFragment = (DashboardFragment) fragmentManager.findFragmentByTag("2");
                }else {
                    dashboardFragment = (DashboardFragment) mainActivity.getFragment(2);
                }
                mainActivity.setFragment(dashboardFragment, "2", 0);
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

        //Кнопка перехода в школу ходьбы
        ShoolStepButton = view.findViewById(R.id.ShoolStepButton);
        ShoolStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager.findFragmentByTag("3")!=null) {
                    notificationsFragment = (NotificationsFragment) fragmentManager.findFragmentByTag("3");
                }else {
                    notificationsFragment = (NotificationsFragment) mainActivity.getFragment(3);
                }
                mainActivity.setFragment(notificationsFragment,"3",2);
            }
        });

        //Второй раздел название
        TextviewSectionName2 = view.findViewById(R.id.TextviewSectionName2);
        MainActivity.LoadImageProfile(mAuth.getCurrentUser(),imageviewprofile,getActivity());


        DownloadHomeFragmentData();


    }


    @Override
    public void onStart() {
        super.onStart();
        VideoStart();
    }

    private void DownloadHomeFragmentData(){
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child("Section1");
        //Загрузка данных о 1 карточке
        mDataBase.child("SectionName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    //Данные получены
                    TextviewSectionName1.setText(task.getResult().getValue().toString());
                    SceletonCardArticleSection.stopLoading();
                }
            }
        });
        mDataBase.child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    //Данные получены
                    Section1ID = task.getResult().getValue().toString();
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

        mDataBase.child("articleList").child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

        mDataBase.child("articleList").child("2").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

        mDataBase.child("articleList").child("3").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

        mDataBase.child("articleList").child("4").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                    CardHomeArticle5.setVisibility(View.GONE);
                }
                else {
                    try {
                        //Данные получены
                        DowArticle5 =  task.getResult().getValue(Article.class);
                        if(DowArticle5!= null){
                            TextHomeArticle5.setText(Html.fromHtml(DowArticle5.HeadText).toString().trim());
                            Glide.with(getActivity()).load(DowArticle5.PreviewPhotoUri).into(ImageHomeArticle5);
                        } else {
                            CardHomeArticle5.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        DownloadArticleFirebaseData(FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child("Section2").child("articleList"));
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(HomeArticle_Key).child("Section2");
        mDataBase.child("SectionName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    //Данные получены
                    TextviewSectionName2.setText(task.getResult().getValue().toString());
                    CardArticleSection2.stopLoading();
                }
            }
        });
        mDataBase.child("SectionID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    //Данные получены
                    Section2ID = task.getResult().getValue().toString();
                }
            }
        });
    }

    //Нажатие на карточку статьи
    @Override
    public void onItemClick(View view, int position) {
        if(ArticlelistTemp.get(position)!=null) {
            MainActivity.LoadArticleFragment(ArticlelistTemp.get(position) ,getActivity().getSupportFragmentManager(),R.id.HomeFragment);
        }
    }
}